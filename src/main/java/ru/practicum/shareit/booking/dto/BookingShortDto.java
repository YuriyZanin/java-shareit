package ru.practicum.shareit.booking.dto;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
public class BookingShortDto {
    private final Long id;
    private final Long bookerId;
}
