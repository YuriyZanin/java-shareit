package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static List<ItemDto> toItemDtos(List<Item> items) {
        return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

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
                        : Collections.emptyList()
        );
    }

    public static Item toItem(ItemDto itemDetails, User owner, ItemRequest request) {
        return Item.builder()
                .id(itemDetails.getId())
                .name(itemDetails.getName())
                .description(itemDetails.getDescription())
                .available(itemDetails.getAvailable())
                .owner(owner)
                .request(request)
                .build();
    }
}
