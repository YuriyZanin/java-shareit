package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.validation.CreateValidation;
import ru.practicum.shareit.validation.NullOrNotBlank;
import ru.practicum.shareit.validation.UpdateValidation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Value
public class ItemRequestDto {
    Long id;
    @NotBlank(groups = CreateValidation.class)
    @NullOrNotBlank(groups = UpdateValidation.class)
    String name;
    @NotBlank(groups = CreateValidation.class)
    @NullOrNotBlank(groups = UpdateValidation.class)
    String description;
    @NotNull(groups = CreateValidation.class) Boolean available;
    Long requestId;
}
