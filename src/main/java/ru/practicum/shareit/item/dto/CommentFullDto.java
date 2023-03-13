package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.validation.util.ValidationUtil;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class CommentFullDto {
    private final Long id;
    private final String text;
    private final String authorName;
    @JsonFormat(pattern = ValidationUtil.COMMENT_DATE_TIME_FORMAT)
    private final LocalDateTime created;
}
