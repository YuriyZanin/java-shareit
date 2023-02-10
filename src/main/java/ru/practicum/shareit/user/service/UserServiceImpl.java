package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User get(Long id) {
        return userRepository.get(id).orElseThrow(() -> {
            throw new NotFoundException(String.format("Пользователь с id %d не найден", id));
        });
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User create(User user) {
        checkEmailDuplicated(user.getEmail());
        return userRepository.create(user);
    }

    @Override
    public User update(User user) {
        User actual = get(user.getId());
        if (user.getEmail() != null && !actual.getEmail().equals(user.getEmail())) {
            checkEmailDuplicated(user.getEmail());
        }
        if (user.getName() != null) {
            actual.setName(user.getName());
        }
        if (user.getEmail() != null) {
            actual.setEmail(user.getEmail());
        }
        return userRepository.update(actual);
    }

    @Override
    public boolean delete(Long id) {
        return userRepository.delete(id);
    }

    private void checkEmailDuplicated(String email) {
        Optional<User> user = userRepository.getByEmail(email);
        if (user.isPresent()) {
            throw new AlreadyExistException(String.format("Пользователь с email %s уже есть в базе", email));
        }
    }
}
