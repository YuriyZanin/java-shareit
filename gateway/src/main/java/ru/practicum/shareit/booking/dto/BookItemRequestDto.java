package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.validation.EndDateAfterStartDate;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
@Builder
@EndDateAfterStartDate
public class BookItemRequestDto {
    @NotNull Long itemId;
    @FutureOrPresent LocalDateTime start;
    @Future LocalDateTime end;
}
