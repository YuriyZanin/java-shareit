package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.validation.util.ValidationUtil.checkErrors;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @RequestBody @Valid ItemRequestCreationDto requestDetails, BindingResult errors) {
        checkErrors(errors);
        return itemRequestService.create(userId, requestDetails);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long requestId) {
        return itemRequestService.getById(userId, requestId);
    }

    @GetMapping
    public List<ItemRequestDto> findByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getByUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "20") int size) {
        if (from < 0 || size < 1) {
            throw new ValidationException("Параметры запроса заданы неверно");
        }
        return itemRequestService.getAll(userId, from, size);
    }
}
