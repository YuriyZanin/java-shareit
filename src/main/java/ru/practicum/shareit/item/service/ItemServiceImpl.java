package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item create(Long userId, Item item) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        item.setOwner(owner);
        return itemRepository.save(item);
    }

    @Override
    public Item update(Long userId, Item item) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item actual = itemRepository.findByIdAndOwnerId(item.getId(), userId).orElseThrow(() -> {
            throw new NotFoundException(
                    String.format("У пользователя %s нет вещи с id %d", owner.getName(), item.getId()));
        });

        // Изменить можно название, описание и статус доступа к аренде
        if (item.getName() != null) {
            actual.setName(item.getName());
        }
        if (item.getAvailable() != null) {
            actual.setAvailable(item.getAvailable());
        }
        if (item.getDescription() != null) {
            actual.setDescription(item.getDescription());
        }
        return itemRepository.save(actual);
    }

    @Override
    public Item get(Long userId, Long itemId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        return itemRepository.findById(itemId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Вещь с id %d не найдена", itemId));
        });
    }

    @Override
    public List<Item> getAllByUser(Long userId) {
        return itemRepository.findByOwnerId(userId);
    }

    @Override
    public void delete(Long userId, Long itemId) {
        itemRepository.deleteByIdAndOwnerId(itemId, userId);
    }

    @Override
    public List<Item> getByText(Long userId, String text) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.searchByText(text);
    }
}
