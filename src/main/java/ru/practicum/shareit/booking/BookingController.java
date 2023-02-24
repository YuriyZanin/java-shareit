package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.validation.util.ValidationUtil;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingFullDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @Valid @RequestBody BookingDto bookingDetails, BindingResult errors) {
        ValidationUtil.checkErrors(errors);
        log.info("Запрос на добавление {}", bookingDetails.toString());
        return bookingService.create(userId, bookingDetails);
    }

    @PatchMapping("/{bookingId}")
    public BookingFullDto approveStatus(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @PathVariable long bookingId, @RequestParam boolean approved) {
        log.info("Запрос на подтверждение или отклонение статуса бронирования");
        return bookingService.approveStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingFullDto get(@RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable long bookingId) {
        log.info("Запрос бронироавния по id {}", bookingId);
        return bookingService.get(userId, bookingId);
    }

    @GetMapping
    public Collection<BookingFullDto> findAllByState(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @RequestParam(defaultValue = "ALL") String state) {
        log.info("Запрос на получение списка всех бронирований текущего пользователя");
        return bookingService.getAllWithState(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingFullDto> findAllByOwnerWithState(@RequestHeader("X-Sharer-User-Id") long userId,
                                                              @RequestParam(defaultValue = "ALL") String state) {
        log.info("Запрос на получение списка бронирований для всех вещей текущего пользователя");
        return bookingService.getAllByOwnerWithState(userId, state);
    }
}
