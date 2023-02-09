package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractValidationTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.utils.validation.CreateValidation;
import ru.practicum.shareit.utils.validation.UpdateValidation;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserValidationTest extends AbstractValidationTest {

    @Test
    void shouldBeSuccessValidation() {
        UserDto test = UserDto.builder().email("test@email.com").name("test").build();
        Set<ConstraintViolation<UserDto>> violations = validator.validate(test);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeFailedIfEmailIsIncorrect() {
        UserDto userWithBadMail = UserDto.builder().email("incorrectMail@").name("login").build();
        Set<ConstraintViolation<UserDto>> violations = validator.validate(
                userWithBadMail, CreateValidation.class, UpdateValidation.class);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldBeFailedIfEmailIsNull() {
        UserDto userWithNullMail = UserDto.builder().email(null).name("login").build();
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userWithNullMail, CreateValidation.class);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldBeFailedIfEmailIsEmpty() {
        UserDto userWithEmptyMail = UserDto.builder().email("").name("login").build();
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userWithEmptyMail, CreateValidation.class);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));

        UserDto userWithBlankMail = UserDto.builder().email("   ").name("login").build();
        violations = validator.validate(userWithBlankMail, CreateValidation.class);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldBeFailedIfNameIsNull() {
        UserDto userWithNullName = UserDto.builder().email("test@mail.com").name(null).build();
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userWithNullName, CreateValidation.class);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void shouldBeFailedIfLoginIsBlank() {
        UserDto userWithSpacesName = UserDto.builder().email("test@mail.com").name("   ").build();
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userWithSpacesName, CreateValidation.class);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void shouldBeFailedIfLoginIsEmpty() {
        UserDto userWithEmptyName = UserDto.builder().email("test@mail.com").name("").build();
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userWithEmptyName, CreateValidation.class);
        assertEquals(1, violations.size());
    }
}
