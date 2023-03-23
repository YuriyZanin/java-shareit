package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.validation.CreateValidation;
import ru.practicum.shareit.validation.NullOrNotBlank;
import ru.practicum.shareit.validation.UpdateValidation;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Builder
@Value
public class UserRequestDto {
    Long id;
    @NotBlank(groups = CreateValidation.class)
    @NullOrNotBlank(groups = UpdateValidation.class)
    String name;
    @NotEmpty(groups = CreateValidation.class)
    @Email(groups = {CreateValidation.class, UpdateValidation.class}) String email;
}
