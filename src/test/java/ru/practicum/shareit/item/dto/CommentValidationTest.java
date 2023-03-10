package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractValidationTest;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommentValidationTest extends AbstractValidationTest {
    @Test
    void shouldBeSuccessValidation() {
        CommentCreationDto test = new CommentCreationDto(null, "test");
        Set<ConstraintViolation<CommentCreationDto>> violations = validator.validate(test);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeFailedWhenTextIsNull() {
        CommentCreationDto test = new CommentCreationDto(null, null);
        Set<ConstraintViolation<CommentCreationDto>> violations = validator.validate(test);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("text")));
    }

    @Test
    void shouldBeFailedWhenTextIsEmpty() {
        CommentCreationDto test = new CommentCreationDto(null, "");
        Set<ConstraintViolation<CommentCreationDto>> violations = validator.validate(test);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("text")));
    }

    @Test
    void shouldBeFailedWhenTextIsBlank() {
        CommentCreationDto test = new CommentCreationDto(null, "    ");
        Set<ConstraintViolation<CommentCreationDto>> violations = validator.validate(test);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("text")));
    }
}
