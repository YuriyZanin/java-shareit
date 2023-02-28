package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentFullDto;
import ru.practicum.shareit.item.dto.CommentShortDto;
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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public Item create(Long userId, Item item) {
        User owner = getUser(userId);
        item.setOwner(owner);
        return itemRepository.save(item);
    }

    @Override
    public Item update(Long userId, Item item) {
        User owner = getUser(userId);
        Item actual = itemRepository.findByIdAndOwnerId(item.getId(), userId).orElseThrow(() -> {
            throw new NotFoundException(
                    String.format("У пользователя %s нет вещи с id %d", owner.getName(), item.getId()));
        });

        // Изменить можно название, описание и статус доступа к аренде
        if (item.getName() != null) {
            actual.setName(item.getName());
        }
        if (item.getAvailable() != null) {
            actual.setAvailable(item.getAvailable());
        }
        if (item.getDescription() != null) {
            actual.setDescription(item.getDescription());
        }
        return itemRepository.save(actual);
    }

    @Override
    public ItemFullDto get(Long userId, Long itemId) {
        getUser(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Вещь с id %d не найдена", itemId));
        });
        if (item.getOwner().getId().equals(userId)) {
            Collection<Booking> bookings = bookingRepository
                    .findAllByItemId(itemId, Sort.by("end").descending()).stream()
                    .filter(b -> b.getStatus().equals(Status.APPROVED)).collect(Collectors.toList());
            LocalDateTime now = LocalDateTime.now();
            Booking last = bookings.stream()
                    .filter(b -> b.getEnd().isBefore(now) || b.getEnd().equals(now))
                    .findFirst()
                    .orElse(null);
            Booking next = bookings.stream()
                    .sorted(Comparator.comparing(Booking::getStart))
                    .filter(b -> b.getStart().isAfter(now) || b.getStart().equals(now))
                    .findFirst()
                    .orElse(null);
            return ItemMapper.toItemFullDto(item, last, next);
        } else {
            return ItemMapper.toItemFullDto(item, null, null);
        }
    }

    @Override
    public List<ItemFullDto> getAllByUser(Long userId) {
        List<Item> items = itemRepository.findByOwnerId(userId);
        return items.stream().map(i -> get(userId, i.getId())).collect(Collectors.toList());
    }

    @Override
    public void delete(Long userId, Long itemId) {
        itemRepository.deleteByIdAndOwnerId(itemId, userId);
    }

    @Override
    public List<Item> getByText(Long userId, String text) {
        getUser(userId);
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.searchByText(text);
    }

    @Override
    public CommentFullDto addComment(long userId, long itemId, CommentShortDto comment) {
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
}
