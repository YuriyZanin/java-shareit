package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.utils.exception.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InMemoryItemRepository implements ItemRepository {
    private final UserRepository userRepository;
    private final Map<Long, Map<Long, Item>> itemsByUser = new HashMap<>();
    private long lastId = 0;

    @Override
    public Item create(Long userId, Item item) {
        User owner = userRepository.get(userId);
        item.setId(getId());
        item.setOwner(owner);

        itemsByUser.compute(userId, (user, userItems) -> {
            if (userItems == null) {
                userItems = new HashMap<>();
            }
            userItems.put(item.getId(), item);
            return userItems;
        });
        return item;
    }

    @Override
    public Item update(Long userId, Item item) {
        User owner = userRepository.get(userId);
        if (itemsByUser.containsKey(userId) && itemsByUser.get(userId).containsKey(item.getId())) {
            Item actual = itemsByUser.get(userId).get(item.getId());
            itemsByUser.get(userId).put(item.getId(), ItemMapper.updateFrom(actual, item));
            return actual;
        } else {
            throw new NotFoundException(
                    String.format("У пользователя %s нет вещи с id %d", owner.getEmail(), item.getId()));
        }
    }

    @Override
    public Item get(Long userId, Long itemId) {
        userRepository.get(userId);
        return itemsByUser.values().stream().flatMap(u -> u.values().stream())
                .filter(item -> Objects.equals(item.getId(), itemId))
                .findFirst().orElseThrow(() -> new NotFoundException(String.format("Вещь с id %d не найдена", itemId)));
    }

    @Override
    public List<Item> getByUser(Long userId) {
        userRepository.get(userId);
        if (itemsByUser.get(userId) == null)
            return Collections.emptyList();
        return new ArrayList<>(itemsByUser.get(userId).values());
    }

    @Override
    public List<Item> getByText(Long userId, String text) {
        userRepository.get(userId);
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemsByUser.values().stream().flatMap(u -> u.values().stream())
                .filter(item -> isContainsText(item, text) && item.getAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long userId, Long itemId) {
        User owner = userRepository.get(userId);
        if (itemsByUser.containsKey(userId) && itemsByUser.get(userId).containsKey(itemId)) {
            itemsByUser.get(userId).remove(itemId);
        } else {
            throw new NotFoundException(String.format("У пользователя %s нет вещи с id %d", owner.getEmail(), itemId));
        }
    }

    private long getId() {
        return ++lastId;
    }

    private boolean isContainsText(Item item, String text) {
        return item.getName().toLowerCase().contains(text.toLowerCase())
                || item.getDescription().toLowerCase().contains(text.toLowerCase());
    }
}
