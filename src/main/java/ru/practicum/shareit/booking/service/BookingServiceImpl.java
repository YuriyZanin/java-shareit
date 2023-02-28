package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingFullDto create(long userId, BookingDto bookingDetails) {
        User user = getUser(userId);
        Item item = getItem(bookingDetails.getItemId(), userId);
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь не доступна для бронирования");
        }
        Booking booking = BookingMapper.toBooking(bookingDetails, user, item, Status.WAITING);
        return BookingMapper.toBookingFullDto(bookingRepository.save(booking));
    }

    @Override
    public BookingFullDto approveStatus(long userId, long bookingId, boolean approved) {
        User owner = getUser(userId);
        Booking booking = bookingRepository.findById(
                bookingId).orElseThrow(() -> new NotFoundException("Бронь не найдена"));
        if (booking.getItem().getOwner().getId() != userId) {
            throw new NotFoundException(String.format(
                    "Пользователь %s не является владельцем вещи %s", owner.getName(), booking.getItem().getName()));
        }
        if (booking.getStatus().equals(Status.APPROVED) && approved) {
            throw new ValidationException("Статус уже подтвержден");
        }
        if (booking.getStatus().equals(Status.REJECTED) && !approved) {
            throw new ValidationException("Статус уже откланен");
        }

        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return BookingMapper.toBookingFullDto(bookingRepository.save(booking));
    }

    @Override
    public BookingFullDto get(long userId, long bookingId) {
        User user = getUser(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронь не найдена"));

        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new NotFoundException(String.format(
                    "Пользователь %s не является ни автором ни владельцем вещи %s",
                    user.getName(), booking.getItem().getName()));
        }
        return BookingMapper.toBookingFullDto(booking);
    }

    @Override
    public Collection<BookingFullDto> getAllWithState(long userId, String state) {
        getUser(userId);
        Collection<Booking> bookings = bookingRepository.findAllByBookerId(userId, Sort.by("start").descending());
        return getByState(bookings, state).stream().map(BookingMapper::toBookingFullDto).collect(Collectors.toList());
    }

    @Override
    public Collection<BookingFullDto> getAllByOwnerWithState(long userId, String state) {
        getUser(userId);
        List<Item> items = itemRepository.findByOwnerId(userId);
        Collection<Booking> bookings = bookingRepository.findAllByItemIdIn(items.stream()
                .map(Item::getId)
                .collect(Collectors.toList()), Sort.by("start").descending());
        return getByState(bookings, state).stream().map(BookingMapper::toBookingFullDto).collect(Collectors.toList());
    }

    private User getUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    private Item getItem(long itemId, long userId) {
        return itemRepository.findByIdAndOwnerIdNot(itemId, userId).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
    }

    private Collection<Booking> getByState(Collection<Booking> bookings, String state) {
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case "ALL":
                return bookings;
            case "CURRENT":
                return bookings.stream()
                        .filter(b -> now.isAfter(b.getStart()) && now.isBefore(b.getEnd()))
                        .collect(Collectors.toList());
            case "PAST":
                return bookings.stream().filter(b -> now.isAfter(b.getEnd()))
                        .collect(Collectors.toList());
            case "FUTURE":
                return bookings.stream().filter(b -> now.isBefore(b.getStart()))
                        .collect(Collectors.toList());
            case "WAITING":
                return bookings.stream().filter(b -> b.getStatus().equals(Status.WAITING))
                        .collect(Collectors.toList());
            case "REJECTED":
                return bookings.stream().filter(b -> b.getStatus().equals(Status.REJECTED))
                        .collect(Collectors.toList());
            default:
                throw new ValidationException("Unknown state: " + state);
        }
    }
}
