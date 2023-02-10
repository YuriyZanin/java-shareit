package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.validation.CreateValidation;
import ru.practicum.shareit.validation.UpdateValidation;

import java.util.Collection;
import java.util.stream.Collectors;

import static ru.practicum.shareit.validation.util.ValidationUtil.checkErrors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        log.info("Запрос на получение пользователя по id {}", id);
        return UserMapper.toUserDto(userService.get(id));
    }

    @GetMapping
    public Collection<UserDto> findAll() {
        log.info("Запрос на получение всех пользователей");
        return userService.getAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @PostMapping
    public UserDto create(@Validated(CreateValidation.class) @RequestBody UserDto userDetails, BindingResult errors) {
        checkErrors(errors);
        log.info("Запрос на создание пользователя {}", userDetails.getEmail());
        return UserMapper.toUserDto(userService.create(UserMapper.toUser(userDetails)));
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId,
                          @Validated(UpdateValidation.class) @RequestBody UserDto userDto, BindingResult errors) {
        checkErrors(errors);
        log.info("Запрос на обновление пользователя {}", userDto.toString());
        return UserMapper.toUserDto(userService.update(UserMapper.toUser(userId, userDto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("Запрос на удаление пользователя по id {}", id);
        userService.delete(id);
    }
}
