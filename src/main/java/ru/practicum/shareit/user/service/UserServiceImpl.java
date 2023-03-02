package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto get(long id) {
        User created = userRepository.findById(id).orElseThrow(() -> {
            throw new NotFoundException(String.format("Пользователь с id %d не найден", id));
        });
        return UserMapper.toUserDto(created);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDto create(UserDto userDetails) {
        User user = UserMapper.toUser(userDetails);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Transactional
    @Override
    public UserDto update(long userId, UserDto userDetails) {
        User actual = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException(String.format("Пользователь с id %d не найден", userId));
        });
        if (userDetails.getEmail() != null && !actual.getEmail().equals(userDetails.getEmail())) {
            checkEmailDuplicated(userDetails.getEmail());
        }
        if (userDetails.getName() != null) {
            actual.setName(userDetails.getName());
        }
        if (userDetails.getEmail() != null) {
            actual.setEmail(userDetails.getEmail());
        }
        return UserMapper.toUserDto(userRepository.save(actual));
    }

    @Transactional
    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    private void checkEmailDuplicated(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isPresent()) {
            throw new AlreadyExistException(String.format("Пользователь с email %s уже есть в базе", email));
        }
    }
}
