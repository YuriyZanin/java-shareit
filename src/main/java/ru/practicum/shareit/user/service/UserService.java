package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    User get(long id);

    List<User> getAll();

    User create(User user);

    User update(User user);

    void delete(long id);

}
