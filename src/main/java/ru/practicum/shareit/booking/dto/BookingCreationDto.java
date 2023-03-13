package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.validation.EndDateAfterStartDate;
import ru.practicum.shareit.validation.util.ValidationUtil;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor
@EndDateAfterStartDate
public class BookingCreationDto {
    @NotNull
    private final Long itemId;
    @FutureOrPresent
    @JsonFormat(pattern = ValidationUtil.DEFAULT_DATE_TIME_FORMAT)
    private final LocalDateTime start;
    @FutureOrPresent
    @JsonFormat(pattern = ValidationUtil.DEFAULT_DATE_TIME_FORMAT)
    private final LocalDateTime end;
}
