package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.CommentFullDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(long userId, Item item);

    Item update(long userId, Item item);

    ItemFullDto get(long userId, long itemId);

    List<ItemFullDto> getAllByUser(long userId);

    void delete(long userId, long itemId);

    List<Item> getByText(long userId, String text);

    CommentFullDto addComment(long userId, long itemId, CommentCreationDto comment);
}
