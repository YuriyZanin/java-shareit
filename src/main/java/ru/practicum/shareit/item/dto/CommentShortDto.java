package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter @ToString
@AllArgsConstructor
public class CommentShortDto {
    private final Long id;
    @NotBlank
    @NotNull
    private final String text;
}
