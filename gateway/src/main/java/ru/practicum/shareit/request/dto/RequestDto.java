package ru.practicum.shareit.request.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class RequestDto {
    Long id;
    @NotBlank String description;
}
