package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(Long userId, ItemDto item);

    Item update(Long userId, Long itemId, ItemDto itemDetails);

    Item get(Long userId, Long itemId);

    List<Item> getByUser(Long userId);

    void delete(Long userId, Long itemId);

    List<Item> getByText(Long userId, String text);
}
