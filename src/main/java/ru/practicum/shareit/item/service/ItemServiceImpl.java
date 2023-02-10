package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;

    @Override
    public Item create(Long userId, Item item) {
        return itemRepository.create(userId, item);
    }

    @Override
    public Item update(Long userId, Item item) {
        return itemRepository.update(userId, item);
    }

    @Override
    public Item get(Long userId, Long itemId) {
        return itemRepository.get(userId, itemId);
    }

    @Override
    public List<Item> getByUser(Long userId) {
        return itemRepository.getByUser(userId);
    }

    @Override
    public void delete(Long userId, Long itemId) {
        itemRepository.delete(userId, itemId);
    }

    @Override
    public List<Item> getByText(Long userId, String text) {
        return itemRepository.getByText(userId, text);
    }
}
