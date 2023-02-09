package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.utils.validation.ValidationUtil.checkErrors;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    public final ItemService itemService;

    @PostMapping
    public Item create(@RequestHeader("X-Sharer-User-Id") long userId,
                       @Valid @RequestBody ItemDto itemDetails, BindingResult errors) {
        checkErrors(errors);
        log.info("Запрос на добавление {}", itemDetails.getName());
        return itemService.create(userId, itemDetails);
    }

    @PatchMapping("/{itemId}")
    public Item update(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                       @RequestBody ItemDto itemDetails) {
        log.info("Запрос на изменение {} у пользователя с id {}", itemDetails.getName(), userId);
        return itemService.update(userId, itemId, itemDetails);
    }

    @GetMapping("/{itemId}")
    public Item get(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        log.info("Запрос вещи с id {} у пользователя с id {}", itemId, userId);
        return itemService.get(userId, itemId);
    }

    @GetMapping
    public List<Item> findByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Запрос всех вещей у пользователя с id {}", userId);
        return itemService.getByUser(userId);
    }

    @GetMapping("/search")
    public List<Item> findByNameAndDescription(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam String text) {
        log.info("Запрос на поиск вещей содержащих: {}", text);
        return itemService.getByText(userId, text);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        log.info("Запрос на удаление вещи с id {} у пользователя с id {}", itemId, userId);
        itemService.delete(userId, itemId);
    }
}
