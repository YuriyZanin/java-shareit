package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.AbstractValidationTest;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.validation.CreateValidation;
import ru.practicum.shareit.validation.UpdateValidation;

import javax.validation.ConstraintViolation;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemValidationTest extends AbstractValidationTest {
    @Test
    void shouldBeSuccessValidation() {
        ItemRequestDto itemDto = ItemRequestDto.builder().name("test").description("desc").available(true).build();

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemDto, CreateValidation.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeFailedWhenCreateWithNullName() {
        ItemRequestDto itemDto = ItemRequestDto.builder().name(null).description("desc").available(true).build();

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemDto, CreateValidation.class);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void shouldBeSuccessWhenUpdateWithNullName() {
        ItemRequestDto itemDto = ItemRequestDto.builder().name(null).description("desc").available(true).build();

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemDto, UpdateValidation.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeFailedIfNameIsEmpty() {
        ItemRequestDto itemDto = ItemRequestDto.builder().name("").description("desc").available(true).build();

        Set<ConstraintViolation<ItemRequestDto>> createViolations = validator.validate(itemDto, CreateValidation.class);
        Set<ConstraintViolation<ItemRequestDto>> updateViolations = validator.validate(itemDto, UpdateValidation.class);

        assertEquals(1, createViolations.size());
        assertTrue(createViolations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
        assertEquals(1, updateViolations.size());
        assertTrue(updateViolations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void shouldBeFailedIfNameIsBlank() {
        ItemRequestDto itemDto = ItemRequestDto.builder().name("   ").description("desc").available(true).build();

        Set<ConstraintViolation<ItemRequestDto>> createViolations = validator.validate(itemDto, CreateValidation.class);
        Set<ConstraintViolation<ItemRequestDto>> updateViolations = validator.validate(itemDto, UpdateValidation.class);

        assertEquals(1, createViolations.size());
        assertTrue(createViolations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
        assertEquals(1, updateViolations.size());
        assertTrue(updateViolations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void shouldBeFailedWhenCreateWithNullDescription() {
        ItemRequestDto itemDto = ItemRequestDto.builder().name("name").description(null).available(true).build();

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemDto, CreateValidation.class);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void shouldBeSuccessWhenUpdateWithNullDescription() {
        ItemRequestDto itemDto = ItemRequestDto.builder().name("name").description(null).available(true).build();

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemDto, UpdateValidation.class);

        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeFailedIfDescriptionIsEmpty() {
        ItemRequestDto itemDto = ItemRequestDto.builder().name("name").description("").available(true).build();

        Set<ConstraintViolation<ItemRequestDto>> createViolation = validator.validate(itemDto, CreateValidation.class);
        Set<ConstraintViolation<ItemRequestDto>> updateViolation = validator.validate(itemDto, UpdateValidation.class);

        assertEquals(1, createViolation.size());
        assertTrue(createViolation.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
        assertEquals(1, updateViolation.size());
        assertTrue(updateViolation.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void shouldBeFailedIfDescriptionIsBlank() {
        ItemRequestDto itemDto = ItemRequestDto.builder().name("name").description("  ").available(true).build();

        Set<ConstraintViolation<ItemRequestDto>> createViolations = validator.validate(itemDto, CreateValidation.class);
        Set<ConstraintViolation<ItemRequestDto>> updateViolations = validator.validate(itemDto, UpdateValidation.class);

        assertEquals(1, createViolations.size());
        assertTrue(createViolations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
        assertEquals(1, updateViolations.size());
        assertTrue(updateViolations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
    }

    @Test
    void shouldBeFailedWhenCreateWithNullAvailable() {
        ItemRequestDto itemDto = ItemRequestDto.builder().name("name").description("desc").available(null).build();

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemDto, CreateValidation.class);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("available")));
    }

    @Test
    void shouldBeSuccessWhenUpdateWithNullAvailable() {
        ItemRequestDto itemDto = ItemRequestDto.builder().name("name").description("desc").available(null).build();

        Set<ConstraintViolation<ItemRequestDto>> violations = validator.validate(itemDto, UpdateValidation.class);

        assertTrue(violations.isEmpty());
    }
}
