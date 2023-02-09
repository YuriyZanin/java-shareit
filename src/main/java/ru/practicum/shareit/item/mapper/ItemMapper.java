package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    public static Item toItem(Long itemId, ItemDto itemDto) {
        return Item.builder()
                .id(itemId)
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }

    // Изменить можно название, описание и статус доступа к аренде
    public static Item updateFrom(Item actual, Item updated) {
        if (updated.getName() != null) {
            actual.setName(updated.getName());
        }
        if (updated.getAvailable() != null) {
            actual.setAvailable(updated.getAvailable());
        }
        if (updated.getDescription() != null) {
            actual.setDescription(updated.getDescription());
        }
        return actual;
    }
}
