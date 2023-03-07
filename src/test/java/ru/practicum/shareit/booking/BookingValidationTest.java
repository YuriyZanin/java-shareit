package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractValidationTest;
import ru.practicum.shareit.booking.dto.BookingCreationDto;

import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookingValidationTest extends AbstractValidationTest {
    @Test
    void shouldBeSuccessValidation() {
        BookingCreationDto test = BookingCreationDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusHours(1))
                .build();
        Set<ConstraintViolation<BookingCreationDto>> violations = validator.validate(test);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeFailedWhenItemIdIsNull() {
        BookingCreationDto test = BookingCreationDto.builder()
                .itemId(null)
                .start(LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusHours(1))
                .build();
        Set<ConstraintViolation<BookingCreationDto>> violations = validator.validate(test);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("itemId")));
    }

    @Test
    void shouldBeFailedWhenStartInPast() {
        BookingCreationDto test = BookingCreationDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().minusMinutes(1))
                .end(LocalDateTime.now().plusMinutes(1))
                .build();
        Set<ConstraintViolation<BookingCreationDto>> violations = validator.validate(test);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("start")));
    }

    @Test
    void shouldBeFailedWhenStartAndEndInPast() {
        BookingCreationDto test = BookingCreationDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().minusMinutes(1))
                .end(LocalDateTime.now().minusMinutes(1))
                .build();
        Set<ConstraintViolation<BookingCreationDto>> violations = validator.validate(test);
        assertEquals(3, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("start")));
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("end")));
    }

    @Test
    void shouldBeFailedWhenStartAfterEnd() {
        BookingCreationDto test = BookingCreationDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusMinutes(2))
                .end(LocalDateTime.now().plusMinutes(1))
                .build();
        Set<ConstraintViolation<BookingCreationDto>> violations = validator.validate(test);
        assertEquals(1, violations.size());
    }
}
