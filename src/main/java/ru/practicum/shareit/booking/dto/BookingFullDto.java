package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.validation.util.ValidationUtil;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Builder
public class BookingFullDto {
    private final Long id;
    @JsonFormat(pattern = ValidationUtil.DEFAULT_DATE_TIME_FORMAT)
    private final LocalDateTime start;
    @JsonFormat(pattern = ValidationUtil.DEFAULT_DATE_TIME_FORMAT)
    private final LocalDateTime end;
    private final Status status;
    private final UserDto booker;
    private final ItemFullDto item;
}
