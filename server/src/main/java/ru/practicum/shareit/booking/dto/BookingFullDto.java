package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.util.JsonUtil;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@Builder
public class BookingFullDto {
    private final Long id;
    @JsonFormat(pattern = JsonUtil.DEFAULT_DATE_TIME_FORMAT)
    private final LocalDateTime start;
    @JsonFormat(pattern = JsonUtil.DEFAULT_DATE_TIME_FORMAT)
    private final LocalDateTime end;
    private final String status;
    private final UserDto booker;
    private final ItemFullDto item;
}
