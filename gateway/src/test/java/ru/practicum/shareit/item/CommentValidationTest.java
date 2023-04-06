package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractValidationTest;
import ru.practicum.shareit.item.dto.CommentRequestDto;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommentValidationTest extends AbstractValidationTest {
    @Test
    void shouldBeSuccessValidation() {
        CommentRequestDto test = new CommentRequestDto(null, "test");

        Set<ConstraintViolation<CommentRequestDto>> violations = validator.validate(test);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeFailedWhenTextIsNull() {
        CommentRequestDto test = new CommentRequestDto(null, null);

        Set<ConstraintViolation<CommentRequestDto>> violations = validator.validate(test);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("text")));
    }

    @Test
    void shouldBeFailedWhenTextIsEmpty() {
        CommentRequestDto test = new CommentRequestDto(null, "");

        Set<ConstraintViolation<CommentRequestDto>> violations = validator.validate(test);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("text")));
    }

    @Test
    void shouldBeFailedWhenTextIsBlank() {
        CommentRequestDto test = new CommentRequestDto(null, "    ");

        Set<ConstraintViolation<CommentRequestDto>> violations = validator.validate(test);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("text")));
    }
}
