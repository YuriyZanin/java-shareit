package ru.practicum.shareit.user;

import java.util.List;

public interface UserRepository {
    User get(Long id);

    List<User> getAll();

    User create(User user);

    User update(User user);

    boolean delete(Long id);
}
