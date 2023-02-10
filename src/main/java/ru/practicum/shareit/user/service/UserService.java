package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User get(Long id);

    List<User> getAll();

    User create(User user);

    User update(User user);

    boolean delete(Long id);

}
