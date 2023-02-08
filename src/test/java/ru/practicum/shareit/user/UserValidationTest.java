package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractValidationTest;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserValidationTest extends AbstractValidationTest {

    @Test
    void shouldBeSuccessValidation() {
        User test = User.builder().email("test@email.com").name("test").build();
        Set<ConstraintViolation<User>> violations = validator.validate(test);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeFailedIfEmailIsIncorrect() {
        User userWithBadMail = User.builder().email("incorrectMail@").name("login").build();
        Set<ConstraintViolation<User>> violations = validator.validate(userWithBadMail);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldBeFailedIfEmailIsNull() {
        User userWithNullMail = User.builder().email(null).name("login").build();
        Set<ConstraintViolation<User>> violations = validator.validate(userWithNullMail);
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldBeFailedIfEmailIsEmpty() {
        User userWithEmptyMail = User.builder().email("").name("login").build();
        Set<ConstraintViolation<User>> violations = validator.validate(userWithEmptyMail);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));

        User userWithBlankMail = User.builder().email("   ").name("login").build();
        violations = validator.validate(userWithBlankMail);
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void shouldBeFailedIfNameIsNull() {
        User userWithNullName = User.builder().email("test@mail.com").name(null).build();
        Set<ConstraintViolation<User>> violations = validator.validate(userWithNullName);
        assertEquals(2, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void shouldBeFailedIfLoginIsBlank() {
        User userWithSpacesName = User.builder().email("test@mail.com").name("   ").build();
        Set<ConstraintViolation<User>> violations = validator.validate(userWithSpacesName);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void shouldBeFailedIfLoginIsEmpty() {
        User userWithEmptyName = User.builder().email("test@mail.com").name("").build();
        Set<ConstraintViolation<User>> violations = validator.validate(userWithEmptyName);
        assertEquals(1, violations.size());
    }
}
