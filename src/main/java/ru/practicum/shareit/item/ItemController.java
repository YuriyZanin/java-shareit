package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.validation.CreateValidation;
import ru.practicum.shareit.validation.UpdateValidation;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.validation.util.ValidationUtil.checkErrors;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    public final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                          @Validated(CreateValidation.class) @RequestBody ItemDto itemDetails, BindingResult errors) {
        checkErrors(errors);
        log.info("Запрос на добавление {}", itemDetails.getName());
        return ItemMapper.toItemDto(itemService.create(userId, ItemMapper.toItem(itemDetails)));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                          @Validated(UpdateValidation.class) @RequestBody ItemDto itemDetails) {
        log.info("Запрос на изменение {} у пользователя с id {}", itemDetails.getName(), userId);
        return ItemMapper.toItemDto(itemService.update(userId, ItemMapper.toItem(itemId, itemDetails)));
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        log.info("Запрос вещи с id {} у пользователя с id {}", itemId, userId);
        return ItemMapper.toItemDto(itemService.get(userId, itemId));
    }

    @GetMapping
    public List<ItemDto> findByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Запрос всех вещей у пользователя с id {}", userId);
        return itemService.getByUser(userId).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> findByNameAndDescription(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam String text) {
        log.info("Запрос на поиск вещей содержащих: {}", text);
        return itemService.getByText(userId, text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @DeleteMapping("/{itemId}")
    public void delete(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        log.info("Запрос на удаление вещи с id {} у пользователя с id {}", itemId, userId);
        itemService.delete(userId, itemId);
    }
}
