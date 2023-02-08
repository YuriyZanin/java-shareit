package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
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
        if (checkEmailDuplicated(user.getEmail())) {
            throw new AlreadyExistException(String.format("Пользователь с email %s уже есть в базе", user.getEmail()));
        }
        user.setId(getId());
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
    public User update(Long userId, UserDto userDetails) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException(String.format("Пользователь с id %d не найден в базе", userId));
        }

        User actual = users.get(userId);
        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(actual.getEmail())
                && checkEmailDuplicated(userDetails.getEmail())) {
            throw new AlreadyExistException(
                    String.format("Нельзя изменить email на %s т.к. он уже есть в базе", userDetails.getEmail()));
        }

        users.put(userId, UserMapper.updateFromDto(actual, userDetails));
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
