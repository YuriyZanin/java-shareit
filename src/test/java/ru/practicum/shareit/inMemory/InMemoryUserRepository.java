package ru.practicum.shareit.inMemory;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class InMemoryUserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long lastId = 0;

    public Optional<User> get(Long id) {
        if (users.containsKey(id)) {
            return Optional.of(users.get(id));
        } else {
            return Optional.empty();
        }
    }

    public Optional<User> getByEmail(String email) {
        return users.values().stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    public User create(User user) {
        user.setId(getId());
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public boolean delete(Long id) {
        if (users.containsKey(id)) {
            return users.remove(id) != null;
        } else {
            return false;
        }
    }

    private long getId() {
        return ++lastId;
    }
}
