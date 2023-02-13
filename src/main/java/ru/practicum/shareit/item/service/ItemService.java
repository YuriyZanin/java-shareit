package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(Long userId, Item item);

    Item update(Long userId, Item item);

    Item get(Long userId, Long itemId);

    List<Item> getAllByUser(Long userId);

    void delete(Long userId, Long itemId);

    List<Item> getByText(Long userId, String text);
}
