package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ItemRequestDto {
    private final Long id;
    private final String description;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime created;
    private final List<ItemDto> items;
}
