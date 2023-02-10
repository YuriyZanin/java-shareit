package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item create(Long userId, Item item);

    Item update(Long userId, Item item);

    Item get(Long userId, Long itemId);

    List<Item> getByUser(Long userId);

    void delete(Long userId, Long itemId);

    List<Item> getByText(Long userId, String text);
}
