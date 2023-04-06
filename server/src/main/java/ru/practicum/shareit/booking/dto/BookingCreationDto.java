package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.util.JsonUtil;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
public class BookingCreationDto {
    private final Long itemId;
    @JsonFormat(pattern = JsonUtil.DEFAULT_DATE_TIME_FORMAT)
    private final LocalDateTime start;
    @JsonFormat(pattern = JsonUtil.DEFAULT_DATE_TIME_FORMAT)
    private final LocalDateTime end;
}
