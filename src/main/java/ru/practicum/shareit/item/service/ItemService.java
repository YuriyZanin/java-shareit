package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.CommentFullDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;

import java.util.List;

public interface ItemService {
    ItemFullDto create(long userId, ItemDto itemDetails);

    ItemFullDto update(long userId, long itemId, ItemDto itemDetails);

    ItemFullDto get(long userId, long itemId);

    List<ItemFullDto> getAllByUser(long userId, int from, int size);

    void delete(long userId, long itemId);

    List<ItemFullDto> getByText(long userId, String text, int from, int size);

    CommentFullDto addComment(long userId, long itemId, CommentCreationDto comment);
}
