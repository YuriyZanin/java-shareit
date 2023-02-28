package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class BookingFullDto {
    private final Long id;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final Status status;
    private final UserDto booker;
    private final ItemFullDto item;
}
