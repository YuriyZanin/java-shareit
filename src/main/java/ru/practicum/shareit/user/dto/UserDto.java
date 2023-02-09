package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.utils.validation.CreateValidation;
import ru.practicum.shareit.utils.validation.NullOrNotBlank;
import ru.practicum.shareit.utils.validation.UpdateValidation;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
public class UserDto {
    @NotBlank (groups = CreateValidation.class)
    @NullOrNotBlank(groups = UpdateValidation.class)
    private String name;
    @NotEmpty (groups = CreateValidation.class)
    @Email (groups = {CreateValidation.class, UpdateValidation.class})
    private String email;
}
