package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.util.JsonUtil;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class ItemRequestDto {
    private final Long id;
    private final String description;
    @JsonFormat(pattern = JsonUtil.DEFAULT_DATE_TIME_FORMAT)
    private final LocalDateTime created;
    private final List<ItemDto> items;
}

