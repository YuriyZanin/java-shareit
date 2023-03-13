package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractValidationTest;
import ru.practicum.shareit.validation.CreateValidation;
import ru.practicum.shareit.validation.UpdateValidation;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserValidationTest extends AbstractValidationTest {

    @Test
    void shouldBeSuccessValidation() {
        UserDto test = UserDto.builder().email("test@email.com").name("test").build();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(test, CreateValidation.class);

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
    void shouldBeSuccessWhenUpdateWithNullEmail() {
        UserDto userWithNullMail = UserDto.builder().name("update").build();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userWithNullMail, UpdateValidation.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeFailedIfEmailIsEmpty() {
        UserDto userWithEmptyMail = UserDto.builder().email("").name("login").build();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userWithEmptyMail,
                CreateValidation.class, UpdateValidation.class);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldBeFailedIfEmailIsBlank() {
        UserDto userWithBlankMail = UserDto.builder().email("   ").name("login").build();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userWithBlankMail, CreateValidation.class,
                UpdateValidation.class);

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
    void shouldBeSuccessWhenUpdateWithNullName() {
        UserDto userWithNullName = UserDto.builder().email("test@mail.com").name(null).build();

        Set<ConstraintViolation<UserDto>> violations = validator.validate(userWithNullName, UpdateValidation.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeFailedIfLoginIsBlank() {
        UserDto userWithSpacesName = UserDto.builder().email("test@mail.com").name("   ").build();

        Set<ConstraintViolation<UserDto>> createViolations = validator.validate(userWithSpacesName,
                CreateValidation.class);
        Set<ConstraintViolation<UserDto>> updateViolations = validator.validate(userWithSpacesName,
                UpdateValidation.class);

        assertEquals(1, createViolations.size());
        assertEquals(1, updateViolations.size());
        assertTrue(createViolations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void shouldBeFailedIfLoginIsEmpty() {
        UserDto userWithEmptyName = UserDto.builder().email("test@mail.com").name("").build();

        Set<ConstraintViolation<UserDto>> createViolations = validator.validate(userWithEmptyName,
                CreateValidation.class);
        Set<ConstraintViolation<UserDto>> updateViolations = validator.validate(userWithEmptyName,
                UpdateValidation.class);

        assertEquals(1, createViolations.size());
        assertEquals(1, updateViolations.size());
    }
}
