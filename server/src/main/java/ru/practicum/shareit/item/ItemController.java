package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.CommentFullDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    public final ItemService itemService;

    @PostMapping
    public ItemFullDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                              @RequestBody ItemDto itemDetails) {
        log.info("Запрос на добавление {}", itemDetails.getName());
        return itemService.create(userId, itemDetails);
    }

    @PatchMapping("/{itemId}")
    public ItemFullDto update(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                              @RequestBody ItemDto itemDetails) {
        log.info("Запрос на изменение {} у пользователя с id {}", itemDetails.toString(), userId);
        return itemService.update(userId, itemId, itemDetails);
    }

    @GetMapping("/{itemId}")
    public ItemFullDto get(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        log.info("Запрос вещи с id {} у пользователя с id {}", itemId, userId);
        return itemService.get(userId, itemId);
    }

    @GetMapping
    public List<ItemFullDto> findByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "20") int size) {
        log.info("Запрос всех вещей у пользователя с id {}", userId);
        return itemService.getAllByUser(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemFullDto> findByNameAndDescription(@RequestHeader("X-Sharer-User-Id") long userId,
                                                      @RequestParam String text,
                                                      @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "20") int size) {
        log.info("Запрос на поиск вещей содержащих: {}", text);
        return itemService.getByText(userId, text, from, size);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        log.info("Запрос на удаление вещи с id {} у пользователя с id {}", itemId, userId);
        itemService.delete(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentFullDto addComment(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                                     @RequestBody CommentCreationDto comment) {
        log.info("Запрос на добавление комментария к вещи с id {}", itemId);
        return itemService.addComment(userId, itemId, comment);
    }
}
