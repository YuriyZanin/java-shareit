package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

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
        if (checkEmailDuplicated(user.getEmail())) {
            throw new AlreadyExistException(String.format("Пользователь с email %s уже есть в базе", user.getEmail()));
        }
        user.setId(getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException(String.format("Пользователь с id %d не найден в базе", user.getId()));
        }

        User actual = users.get(user.getId());
        if (user.getEmail() != null && !user.getEmail().equals(actual.getEmail())
                && checkEmailDuplicated(user.getEmail())) {
            throw new AlreadyExistException(
                    String.format("Пользователь с email %s уже есть в базе", user.getEmail()));
        }

        users.put(user.getId(), UserMapper.updateFrom(actual, user));
        return actual;
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

    private boolean checkEmailDuplicated(String email) {
        return users.values().stream().anyMatch(u -> u.getEmail().equals(email));
    }
}
