package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractValidationTest;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.validation.CreateValidation;
import ru.practicum.shareit.validation.UpdateValidation;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserValidationTest extends AbstractValidationTest {

    @Test
    void shouldBeSuccessValidation() {
        UserRequestDto test = UserRequestDto.builder().email("test@email.com").name("test").build();

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(test, CreateValidation.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeFailedIfEmailIsIncorrect() {
        UserRequestDto userWithBadMail = UserRequestDto.builder().email("incorrectMail@").name("login").build();

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(
                userWithBadMail, CreateValidation.class, UpdateValidation.class);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldBeFailedIfEmailIsNull() {
        UserRequestDto userWithNullMail = UserRequestDto.builder().email(null).name("login").build();

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userWithNullMail, CreateValidation.class);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldBeSuccessWhenUpdateWithNullEmail() {
        UserRequestDto userWithNullMail = UserRequestDto.builder().name("update").build();

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userWithNullMail, UpdateValidation.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeFailedIfEmailIsEmpty() {
        UserRequestDto userWithEmptyMail = UserRequestDto.builder().email("").name("login").build();

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userWithEmptyMail,
                CreateValidation.class, UpdateValidation.class);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldBeFailedIfEmailIsBlank() {
        UserRequestDto userWithBlankMail = UserRequestDto.builder().email("   ").name("login").build();

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userWithBlankMail, CreateValidation.class,
                UpdateValidation.class);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldBeFailedIfNameIsNull() {
        UserRequestDto userWithNullName = UserRequestDto.builder().email("test@mail.com").name(null).build();

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userWithNullName, CreateValidation.class);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void shouldBeSuccessWhenUpdateWithNullName() {
        UserRequestDto userWithNullName = UserRequestDto.builder().email("test@mail.com").name(null).build();

        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userWithNullName, UpdateValidation.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeFailedIfLoginIsBlank() {
        UserRequestDto userWithSpacesName = UserRequestDto.builder().email("test@mail.com").name("   ").build();

        Set<ConstraintViolation<UserRequestDto>> createViolations = validator.validate(userWithSpacesName,
                CreateValidation.class);
        Set<ConstraintViolation<UserRequestDto>> updateViolations = validator.validate(userWithSpacesName,
                UpdateValidation.class);

        assertEquals(1, createViolations.size());
        assertEquals(1, updateViolations.size());
        assertTrue(createViolations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void shouldBeFailedIfLoginIsEmpty() {
        UserRequestDto userWithEmptyName = UserRequestDto.builder().email("test@mail.com").name("").build();

        Set<ConstraintViolation<UserRequestDto>> createViolations = validator.validate(userWithEmptyName,
                CreateValidation.class);
        Set<ConstraintViolation<UserRequestDto>> updateViolations = validator.validate(userWithEmptyName,
                UpdateValidation.class);

        assertEquals(1, createViolations.size());
        assertEquals(1, updateViolations.size());
    }
}
