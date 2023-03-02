package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.CreateValidation;
import ru.practicum.shareit.validation.UpdateValidation;

import java.util.Collection;

import static ru.practicum.shareit.validation.util.ValidationUtil.checkErrors;

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
    public UserDto create(@Validated(CreateValidation.class) @RequestBody UserDto userDetails, BindingResult errors) {
        checkErrors(errors);
        log.info("Запрос на создание пользователя {}", userDetails.getEmail());
        return userService.create(userDetails);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable long userId,
                          @Validated(UpdateValidation.class) @RequestBody UserDto userDetails, BindingResult errors) {
        checkErrors(errors);
        log.info("Запрос на обновление пользователя {}", userDetails.toString());
        return userService.update(userId, userDetails);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        log.info("Запрос на удаление пользователя по id {}", id);
        userService.delete(id);
    }
}
