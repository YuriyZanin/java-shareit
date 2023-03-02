package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.validation.EndDateAfterStartDate;

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
    private final LocalDateTime start;
    @FutureOrPresent
    private final LocalDateTime end;
}
