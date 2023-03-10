package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.validation.CreateValidation;
import ru.practicum.shareit.validation.NullOrNotBlank;
import ru.practicum.shareit.validation.UpdateValidation;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
public class UserDto {
    private final Long id;
    @NotBlank (groups = CreateValidation.class)
    @NullOrNotBlank(groups = UpdateValidation.class)
    private final String name;
    @NotEmpty (groups = CreateValidation.class)
    @Email (groups = {CreateValidation.class, UpdateValidation.class})
    private final String email;
}
