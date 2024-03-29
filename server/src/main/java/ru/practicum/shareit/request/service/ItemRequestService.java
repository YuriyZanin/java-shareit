package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(long userId, ItemRequestCreationDto requestDetails);

    List<ItemRequestDto> getByUser(long userId);

    ItemRequestDto getById(long userId, long requestId);

    List<ItemRequestDto> getAll(long userId, int from, int size);
}
