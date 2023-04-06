package ru.practicum.shareit.inMemory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class InMemoryItemRepository {
    private final InMemoryUserRepository userRepository;
    private final Map<Long, Map<Long, Item>> itemsByUser = new HashMap<>();
    private long lastId = 0;

    public Item create(Item item) {
        item.setId(getId());
        itemsByUser.compute(item.getOwner().getId(), (user, userItems) -> {
            if (userItems == null) {
                userItems = new HashMap<>();
            }
            userItems.put(item.getId(), item);
            return userItems;
        });
        return item;
    }

    public Item update(Item item) {
        itemsByUser.get(item.getOwner().getId()).put(item.getId(), item);
        return item;
    }

    public Optional<Item> get(Long itemId) {
        return itemsByUser.values().stream().flatMap(u -> u.values().stream())
                .filter(item -> Objects.equals(item.getId(), itemId))
                .findFirst();
    }

    public Optional<Item> getByUser(Long userId, Long itemId) {
        if (itemsByUser.containsKey(userId) && itemsByUser.get(userId).containsKey(itemId)) {
            return Optional.of(itemsByUser.get(userId).get(itemId));
        } else {
            return Optional.empty();
        }
    }

    public List<Item> getAllByUser(Long userId) {
        userRepository.get(userId);
        if (itemsByUser.get(userId) == null)
            return Collections.emptyList();
        return new ArrayList<>(itemsByUser.get(userId).values());
    }

    public List<Item> getByText(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemsByUser.values().stream().flatMap(u -> u.values().stream())
                .filter(item -> isContainsText(item, text) && item.getAvailable())
                .collect(Collectors.toList());
    }

    public boolean delete(Long userId, Long itemId) {
        if (itemsByUser.containsKey(userId) && itemsByUser.get(userId).containsKey(itemId)) {
            return itemsByUser.get(userId).remove(itemId) != null;
        } else {
            return false;
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
