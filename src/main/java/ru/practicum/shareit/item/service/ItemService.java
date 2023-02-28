package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentFullDto;
import ru.practicum.shareit.item.dto.CommentShortDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(Long userId, Item item);

    Item update(Long userId, Item item);

    ItemFullDto get(Long userId, Long itemId);

    List<ItemFullDto> getAllByUser(Long userId);

    void delete(Long userId, Long itemId);

    List<Item> getByText(Long userId, String text);

    CommentFullDto addComment(long userId, long itemId, CommentShortDto comment);
}
