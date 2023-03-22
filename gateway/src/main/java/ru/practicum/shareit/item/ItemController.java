package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.validation.CreateValidation;
import ru.practicum.shareit.validation.UpdateValidation;
import ru.practicum.shareit.validation.util.ValidationUtil;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable Long itemId) {
        log.info("Get item {}, userId={}", itemId, userId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = "20") @Positive Integer size) {
        log.info("Get items by user {}, from={}, size={}", userId, from, size);
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam String text,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "20") @Positive Integer size) {
        log.info("Search by text {}, userId={}, from={}, size={}", text, userId, from, size);
        return itemClient.searchItems(userId, text, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @Validated(CreateValidation.class) @RequestBody ItemRequestDto requestDto,
                                          BindingResult errors) {
        ValidationUtil.checkErrors(errors);
        log.info("Create item = {}, userId={}", requestDto, userId);
        return itemClient.addItem(userId, requestDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long itemId,
                                             @Validated(UpdateValidation.class) @RequestBody ItemRequestDto requestDto,
                                             BindingResult errors) {
        ValidationUtil.checkErrors(errors);
        log.info("Update item = {}, userId={}, itemId={}", requestDto, userId, itemId);
        return itemClient.updateItem(userId, itemId, requestDto);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @PathVariable Long itemId) {
        log.info("Delete item with id={}, userId={}", itemId, userId);
        itemClient.deleteItem(userId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long itemId,
                                             @Validated @RequestBody CommentRequestDto requestDto,
                                             BindingResult errors) {
        ValidationUtil.checkErrors(errors);
        log.info("Add comment {}, userId={}, itemId={}", requestDto, userId, itemId);
        return itemClient.addComment(userId, itemId, requestDto);
    }
}
