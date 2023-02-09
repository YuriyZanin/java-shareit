package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.utils.validation.CreateValidation;
import ru.practicum.shareit.utils.validation.UpdateValidation;

import java.util.Collection;

import static ru.practicum.shareit.utils.validation.ValidationUtil.checkErrors;

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
    public User create(@Validated(CreateValidation.class) @RequestBody UserDto userDetails, BindingResult errors) {
        checkErrors(errors);
        log.info("Запрос на создание пользователя {}", userDetails.getEmail());
        return userService.create(UserMapper.toUser(userDetails));
    }

    @PatchMapping("/{userId}")
    public User update(@PathVariable Long userId,
                       @Validated(UpdateValidation.class) @RequestBody UserDto userDto, BindingResult errors) {
        checkErrors(errors);
        log.info("Запрос на обновление пользователя {}", userDto.getEmail());
        return userService.update(UserMapper.toUser(userId, userDto));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Запрос на удаление пользователя по id {}", id);
        userService.delete(id);
    }
}
