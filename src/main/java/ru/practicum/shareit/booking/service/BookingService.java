package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;

import java.util.Collection;

public interface BookingService {

    BookingFullDto create(long userId, BookingDto bookingDetails);

    BookingFullDto approveStatus(long userId, long bookingId, boolean approved);

    BookingFullDto get(long userId, long bookingId);

    Collection<BookingFullDto> getAllWithState(long userId, String state);

    Collection<BookingFullDto> getAllByOwnerWithState(long userId, String state);
}
