package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractValidationTest;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemRequestValidationTest extends AbstractValidationTest {
    @Test
    void shouldBeSuccessValidation() {
        ItemRequestCreationDto creationDto = new ItemRequestCreationDto(null, "description");

        Set<ConstraintViolation<ItemRequestCreationDto>> violations = validator.validate(creationDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeFailedIfDescriptionIsNull() {
        ItemRequestCreationDto creationDto = new ItemRequestCreationDto(null, null);

        Set<ConstraintViolation<ItemRequestCreationDto>> violations = validator.validate(creationDto);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void shouldBeFailedIfDescriptionIsEmpty() {
        ItemRequestCreationDto creationDto = new ItemRequestCreationDto(null, "");

        Set<ConstraintViolation<ItemRequestCreationDto>> violations = validator.validate(creationDto);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void shouldBeFailedIfDescriptionIsBlank() {
        ItemRequestCreationDto creationDto = new ItemRequestCreationDto(null, "   ");

        Set<ConstraintViolation<ItemRequestCreationDto>> violations = validator.validate(creationDto);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }
}
