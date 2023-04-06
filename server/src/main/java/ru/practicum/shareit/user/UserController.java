package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto get(@PathVariable long id) {
        log.info("Запрос на получение пользователя по id {}", id);
        return userService.get(id);
    }

    @GetMapping
    public Collection<UserDto> findAll() {
        log.info("Запрос на получение всех пользователей");
        return userService.getAll();
    }

    @PostMapping
    public UserDto create(@RequestBody UserDto userDetails) {
        log.info("Запрос на создание пользователя {}", userDetails.getEmail());
        return userService.create(userDetails);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable long userId,
                          @RequestBody UserDto userDetails) {
        log.info("Запрос на обновление пользователя {}", userDetails.toString());
        return userService.update(userId, userDetails);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        log.info("Запрос на удаление пользователя по id {}", id);
        userService.delete(id);
    }
}
