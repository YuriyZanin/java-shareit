package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item create(Item item);

    Item update(Item item);

    Optional<Item> get(Long itemId);

    Optional<Item> getByUser(Long userId, Long itemId);

    List<Item> getAllByUser(Long userId);

    boolean delete(Long userId, Long itemId);

    List<Item> getByText(String text);
}
