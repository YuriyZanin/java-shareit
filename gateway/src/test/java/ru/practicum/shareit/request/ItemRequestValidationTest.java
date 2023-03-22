package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractValidationTest;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemRequestValidationTest extends AbstractValidationTest {
    @Test
    void shouldBeSuccessValidation() {
        RequestDto creationDto = new RequestDto(null, "description");

        Set<ConstraintViolation<RequestDto>> violations = validator.validate(creationDto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeFailedIfDescriptionIsNull() {
        RequestDto creationDto = new RequestDto(null, null);

        Set<ConstraintViolation<RequestDto>> violations = validator.validate(creationDto);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void shouldBeFailedIfDescriptionIsEmpty() {
        RequestDto creationDto = new RequestDto(null, "");

        Set<ConstraintViolation<RequestDto>> violations = validator.validate(creationDto);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void shouldBeFailedIfDescriptionIsBlank() {
        RequestDto creationDto = new RequestDto(null, "   ");

        Set<ConstraintViolation<RequestDto>> violations = validator.validate(creationDto);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }
}
