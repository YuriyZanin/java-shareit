package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.model.Item;

import java.util.stream.Collectors;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static ItemFullDto toItemFullDto(Item item, Booking last, Booking next) {
        return new ItemFullDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner().getId(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                last != null
                        ? BookingMapper.toBookingShorDto(last)
                        : null,
                next != null
                        ? BookingMapper.toBookingShorDto(next)
                        : null,
                item.getComments() != null
                        ? item.getComments().stream().map(CommentMapper::toCommentFullDto).collect(Collectors.toList())
                        : null
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
}
