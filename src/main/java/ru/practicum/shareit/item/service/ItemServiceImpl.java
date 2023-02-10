package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    public Item create(Long userId, Item item) {
        User owner = userService.get(userId);
        item.setOwner(owner);
        return itemRepository.create(item);
    }

    @Override
    public Item update(Long userId, Item item) {
        User owner = userService.get(userId);
        Item actual = itemRepository.getByUser(userId, item.getId()).orElseThrow(() -> {
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
        return itemRepository.update(actual);
    }

    @Override
    public Item get(Long userId, Long itemId) {
        userService.get(userId);
        return itemRepository.get(itemId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Вещь с id %d не найдена", itemId));
        });
    }

    @Override
    public List<Item> getAllByUser(Long userId) {
        return itemRepository.getAllByUser(userId);
    }

    @Override
    public boolean delete(Long userId, Long itemId) {
        return itemRepository.delete(userId, itemId);
    }

    @Override
    public List<Item> getByText(Long userId, String text) {
        userService.get(userId);
        return itemRepository.getByText(text);
    }
}
