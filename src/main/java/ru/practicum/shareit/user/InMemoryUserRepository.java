package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.utils.exception.AlreadyExistException;
import ru.practicum.shareit.utils.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long lastId = 0;

    @Override
    public User get(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException(String.format("Пользователь с id %d не найден", id));
        }
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        user.setId(getId());
        if (users.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new AlreadyExistException(String.format("Пользователь с email %s уже есть в базе", user.getEmail()));
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (user.getId() == null || !users.containsKey(user.getId())) {
            throw new NotFoundException(String.format("Пользователь с id %d не найден в базе", user.getId()));
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean delete(Long id) {
        if (users.containsKey(id)) {
            return users.remove(id) != null;
        } else {
            throw new NotFoundException(String.format("Пользователь с id %d не найден в базе", id));
        }
    }

    private long getId() {
        return ++lastId;
    }
}
