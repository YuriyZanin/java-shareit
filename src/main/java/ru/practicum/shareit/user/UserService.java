package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    User get(Long id);

    List<User> getAll();

    User create(User user);

    User update(Long userId, UserDto userDetails);

    boolean delete(Long id);

}
