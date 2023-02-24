package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static BookingFullDto toBookingFullDto(Booking booking) {
        return BookingFullDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(booking.getItem())
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();
    }

    public static Booking toBooking(BookingDto dto, User booker, Item item, Status status) {
        return Booking.builder()
                .start(dto.getStart())
                .end(dto.getEnd())
                .status(status)
                .booker(booker)
                .item(item)
                .build();
    }
}
