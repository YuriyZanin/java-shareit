package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ItemFullDto {
    private final Long id;
    private final String name;
    private final String description;
    private final Boolean available;
    private final Long ownerId;
    private final Long requestId;
    private final BookingShortDto lastBooking;
    private final BookingShortDto nextBooking;
    private final List<CommentFullDto> comments;
}
