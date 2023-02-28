package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.model.State;

import java.util.Collection;

public interface BookingService {

    BookingFullDto create(long userId, BookingDto bookingDetails);

    BookingFullDto approveStatus(long userId, long bookingId, boolean approved);

    BookingFullDto get(long userId, long bookingId);

    Collection<BookingFullDto> getAllByState(long userId, State state);

    Collection<BookingFullDto> getAllByOwnerAndState(long userId, State state);
}
