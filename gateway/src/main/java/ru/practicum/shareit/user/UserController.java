package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.validation.CreateValidation;
import ru.practicum.shareit.validation.UpdateValidation;
import ru.practicum.shareit.validation.util.ValidationUtil;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable Long userId) {
        log.info("Get user {}", userId);
        return userClient.getUser(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        log.info("Get users");
        return userClient.getUsers();
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@Validated(CreateValidation.class) @RequestBody UserRequestDto requestDto,
                                          BindingResult errors) {
        ValidationUtil.checkErrors(errors);
        log.info("Creating user {}", requestDto);
        return userClient.addUser(requestDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId,
                                             @Validated(UpdateValidation.class) @RequestBody UserRequestDto requestDto,
                                             BindingResult errors) {
        ValidationUtil.checkErrors(errors);
        log.info("Updating user {}, {}", userId, requestDto);
        return userClient.updateUser(userId, requestDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable Long userId) {
        log.info("Deleting user {}", userId);
        userClient.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
