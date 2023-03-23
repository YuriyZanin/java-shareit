package ru.practicum.shareit.item.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class CommentRequestDto {
    Long id;
    @NotBlank String text;
}
