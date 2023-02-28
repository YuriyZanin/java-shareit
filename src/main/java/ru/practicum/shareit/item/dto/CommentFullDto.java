package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class CommentFullDto {
    private final Long id;
    private final String text;
    private final String authorName;
    private final LocalDateTime created;
}
