package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.validation.CreateValidation;
import ru.practicum.shareit.validation.NullOrNotBlank;
import ru.practicum.shareit.validation.UpdateValidation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
public class ItemRequestDto {
    private final Long id;
    @NotBlank(groups = CreateValidation.class)
    @NullOrNotBlank(groups = UpdateValidation.class)
    private final String name;
    @NotBlank(groups = CreateValidation.class)
    @NullOrNotBlank(groups = UpdateValidation.class)
    private final String description;
    @NotNull(groups = CreateValidation.class)
    private final Boolean available;
    private final Long requestId;
}
