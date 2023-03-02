package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto get(long id);

    List<UserDto> getAll();

    UserDto create(UserDto userDetails);

    UserDto update(long userId, UserDto userDetails);

    void delete(long id);
}
