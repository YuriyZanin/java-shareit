package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.validation.util.ValidationUtil;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody @Validated RequestDto requestDto,
                                             BindingResult errors) {
        ValidationUtil.checkErrors(errors);
        log.info("Create request {}, userId={}", requestDto, userId);
        return requestClient.addRequest(userId, requestDto);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long requestId) {
        log.info("Get request by id {}, userId={}", requestId, userId);
        return requestClient.getRequest(userId, requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "20") @Positive Integer size) {
        log.info("Get all requests, userId={}, from={}, size={}", userId, from, size);
        return requestClient.getRequests(userId, from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Get request by user, userId={}", userId);
        return requestClient.getRequestsByUser(userId);
    }
}
