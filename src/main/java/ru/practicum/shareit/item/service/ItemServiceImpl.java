package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.CommentFullDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Transactional
    @Override
    public ItemFullDto create(long userId, ItemDto itemDetails) {
        User owner = getUser(userId);
        Item item = ItemMapper.toItem(itemDetails);
        item.setOwner(owner);
        return ItemMapper.toItemFullDto(itemRepository.save(item), null, null);
    }

    @Transactional
    @Override
    public ItemFullDto update(long userId, long itemId, ItemDto itemDetails) {
        User owner = getUser(userId);
        Item actual = itemRepository.findByIdAndOwnerId(itemId, userId).orElseThrow(() -> {
            throw new NotFoundException(
                    String.format("У пользователя %s нет вещи с id %d", owner.getName(), itemId));
        });

        // Изменить можно название, описание и статус доступа к аренде
        if (itemDetails.getName() != null) {
            actual.setName(itemDetails.getName());
        }
        if (itemDetails.getAvailable() != null) {
            actual.setAvailable(itemDetails.getAvailable());
        }
        if (itemDetails.getDescription() != null) {
            actual.setDescription(itemDetails.getDescription());
        }
        return ItemMapper.toItemFullDto(itemRepository.save(actual), null, null);
    }

    @Override
    public ItemFullDto get(long userId, long itemId) {
        getUser(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Вещь с id %d не найдена", itemId));
        });
        if (item.getOwner().getId().equals(userId)) {
            List<Booking> bookings = bookingRepository.findAllByItemIdAndStatus(itemId, Status.APPROVED);
            Booking last = findLastBooking(bookings);
            Booking next = findNextBooking(bookings);
            return ItemMapper.toItemFullDto(item, last, next);
        } else {
            return ItemMapper.toItemFullDto(item, null, null);
        }
    }

    @Override
    public List<ItemFullDto> getAllByUser(long userId) {
        getUser(userId);
        List<Item> items = itemRepository.findByOwnerId(userId);
        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<Booking> allBookings = bookingRepository.findAllByItemIdInAndStatus(items.stream().map(Item::getId)
                .collect(Collectors.toList()), Status.APPROVED);
        Map<Long, List<Booking>> byItemId = allBookings.stream()
                .collect(Collectors.groupingBy(b -> b.getItem().getId()));

        List<ItemFullDto> result = new LinkedList<>();
        for (Item item : items) {
            List<Booking> bookings = byItemId.get(item.getId());
            Booking last = findLastBooking(bookings);
            Booking next = findNextBooking(bookings);
            result.add(ItemMapper.toItemFullDto(item, last, next));
        }
        return result;
    }

    @Transactional
    @Override
    public void delete(long userId, long itemId) {
        itemRepository.deleteByIdAndOwnerId(itemId, userId);
    }

    @Override
    public List<ItemFullDto> getByText(long userId, String text) {
        getUser(userId);
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.searchByText(text).stream()
                .map(i -> ItemMapper.toItemFullDto(i, null, null)).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CommentFullDto addComment(long userId, long itemId, CommentCreationDto comment) {
        User author = getUser(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        List<Booking> bookings = bookingRepository
                .findAllByBookerIdAndItemId(userId, itemId, Sort.by("end"));
        if (bookings == null || bookings.isEmpty()) {
            throw new ValidationException("Пользователь не брал вещь в аренду");
        }
        if (bookings.get(0).getEnd().isAfter(LocalDateTime.now())) {
            throw new ValidationException("Время аренды еще не прошло");
        }

        Comment saved = commentRepository.save(CommentMapper.toComment(comment, author, item, LocalDateTime.now()));
        return CommentMapper.toCommentFullDto(saved);
    }

    private User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private Booking findLastBooking(List<Booking> bookings) {
        if (bookings == null) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        return bookings.stream()
                .sorted(Comparator.comparing(Booking::getEnd).reversed())
                .filter(b -> b.getEnd().isBefore(now) || b.getEnd().equals(now))
                .findFirst()
                .orElse(null);
    }

    private Booking findNextBooking(List<Booking> bookings) {
        if (bookings == null) {
            return null;
        }
        LocalDateTime now = LocalDateTime.now();
        return bookings.stream()
                .sorted(Comparator.comparing(Booking::getStart))
                .filter(b -> b.getStart().isAfter(now) || b.getStart().equals(now))
                .findFirst()
                .orElse(null);
    }
}
