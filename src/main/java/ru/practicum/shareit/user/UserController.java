package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.Collection;

import static ru.practicum.shareit.utils.ValidationUtil.checkErrors;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        log.info("Запрос на получение пользователя по id {}", id);
        return userService.get(id);
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Запрос на получение всех пользователей");
        return userService.getAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user, BindingResult errors) {
        checkErrors(errors);
        log.info("Запрос на создание пользователя {}", user.getEmail());
        return userService.create(user);
    }

    @PatchMapping("/{userId}")
    public User update(@PathVariable Long userId, @Valid @RequestBody UserDto userDto, BindingResult errors) {
        checkErrors(errors);
        log.info("Запрос на обновление пользователя {}", userDto.getEmail());
        return userService.update(userId, userDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Запрос на удаление пользователя по id {}", id);
        userService.delete(id);
    }
}
