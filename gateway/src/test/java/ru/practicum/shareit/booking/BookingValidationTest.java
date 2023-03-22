package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractValidationTest;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingValidationTest extends AbstractValidationTest {
    @Test
    void shouldBeSuccessValidation() {
        BookItemRequestDto test = BookItemRequestDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusHours(1))
                .build();

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(test);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeFailedWhenItemIdIsNull() {
        BookItemRequestDto test = BookItemRequestDto.builder()
                .itemId(null)
                .start(LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusHours(1))
                .build();

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(test);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("itemId")));
    }

    @Test
    void shouldBeFailedWhenStartInPast() {
        BookItemRequestDto test = BookItemRequestDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().minusMinutes(1))
                .end(LocalDateTime.now().plusMinutes(1))
                .build();

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(test);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("start")));
    }

    @Test
    void shouldBeFailedWhenStartAfterEnd() {
        BookItemRequestDto test = BookItemRequestDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusMinutes(2))
                .end(LocalDateTime.now().plusMinutes(1))
                .build();

        Set<ConstraintViolation<BookItemRequestDto>> violations = validator.validate(test);

        assertEquals(1, violations.size());
    }
}
