package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static ru.practicum.shareit.TestData.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void shouldBeFailedIfNotFound() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> userService.get(user1.getId()));
        Assertions.assertThrows(NotFoundException.class, () -> userService.update(user1.getId(), userDto));
    }

    @Test
    void shouldBeFailedIfEmailDuplicated() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findUserByEmail(Mockito.anyString())).thenReturn(Optional.of(user2));
        Mockito.when(userRepository.save(Mockito.any())).thenThrow(DataIntegrityViolationException.class);

        Assertions.assertThrows(AlreadyExistException.class, () -> userService.update(user1.getId(), userDto));
        Assertions.assertThrows(AlreadyExistException.class, () -> userService.create(userDto));
    }
}
