package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractValidationTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.utils.validation.CreateValidation;
import ru.practicum.shareit.utils.validation.UpdateValidation;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemValidationTest extends AbstractValidationTest {
    @Test
    void shouldBeSuccessValidation() {
        ItemDto test = ItemDto.builder().name("test").description("desc").available(true).build();
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(test, CreateValidation.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeFailedWhenCreateWithNullName() {
        ItemDto test = ItemDto.builder().name(null).description("desc").available(true).build();
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(test, CreateValidation.class);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void shouldBeSuccessWhenUpdateWithNullName() {
        ItemDto test = ItemDto.builder().name(null).description("desc").available(true).build();
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(test, UpdateValidation.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeFailedIfNameIsEmpty() {
        ItemDto test = ItemDto.builder().name("").description("desc").available(true).build();
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(test, CreateValidation.class);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));

        violations = validator.validate(test, UpdateValidation.class);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void shouldBeFailedIfNameIsBlank() {
        ItemDto test = ItemDto.builder().name("   ").description("desc").available(true).build();
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(test, CreateValidation.class);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));

        violations = validator.validate(test, UpdateValidation.class);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void shouldBeFailedWhenCreateWithNullDescription() {
        ItemDto test = ItemDto.builder().name("name").description(null).available(true).build();
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(test, CreateValidation.class);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void shouldBeSuccessWhenUpdateWithNullDescription() {
        ItemDto test = ItemDto.builder().name("name").description(null).available(true).build();
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(test, UpdateValidation.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeFailedIfDescriptionIsEmpty() {
        ItemDto test = ItemDto.builder().name("name").description("").available(true).build();
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(test, CreateValidation.class);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));

        violations = validator.validate(test, UpdateValidation.class);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void shouldBeFailedIfDescriptionIsBlank() {
        ItemDto test = ItemDto.builder().name("name").description("  ").available(true).build();
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(test, CreateValidation.class);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));

        violations = validator.validate(test, UpdateValidation.class);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void shouldBeFailedWhenCreateWithNullAvailable() {
        ItemDto test = ItemDto.builder().name("name").description("desc").available(null).build();
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(test, CreateValidation.class);
        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("available")));
    }

    @Test
    void shouldBeSuccessWhenUpdateWithNullAvailable() {
        ItemDto test = ItemDto.builder().name("name").description("desc").available(null).build();
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(test, UpdateValidation.class);
        assertTrue(violations.isEmpty());
    }
}
