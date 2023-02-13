package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> get(Long id);

    Optional<User> getByEmail(String email);

    List<User> getAll();

    User create(User user);

    User update(User user);

    boolean delete(Long id);
}
