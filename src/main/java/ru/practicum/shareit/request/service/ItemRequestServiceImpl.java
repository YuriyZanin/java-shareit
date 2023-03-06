package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Transactional
    @Override
    public ItemRequestDto create(long userId, ItemRequestCreationDto requestDetails) {
        User user = getUser(userId);
        ItemRequest saved = itemRequestRepository.save(
                ItemRequestMapper.toItemRequest(requestDetails, user, LocalDateTime.now()));
        return ItemRequestMapper.toItemRequestDto(saved);
    }

    @Override
    public List<ItemRequestDto> getByUser(long userId) {
        getUser(userId);

        List<ItemRequest> requests = itemRequestRepository.findByRequesterId(
                userId, Sort.by(Sort.Direction.DESC, "created"));

        List<ItemRequestDto> result = new LinkedList<>();
        for (ItemRequest request : requests) {
            List<ItemDto> itemDtos = ItemMapper.toItemDtoList(itemRepository.findByRequestId(request.getId()));
            result.add(ItemRequestMapper.toItemRequestDto(request, itemDtos));
        }
        return result;
    }

    @Override
    public ItemRequestDto getById(long userId, long requestId) {
        getUser(userId);
        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));
        List<ItemDto> itemDtos = ItemMapper.toItemDtoList(itemRepository.findByRequestId(requestId));
        return ItemRequestMapper.toItemRequestDto(request, itemDtos);
    }

    @Override
    public List<ItemRequestDto> getAll(long userId, int from, int size) {
        getUser(userId);

        Sort sortByCreatedDesc = Sort.by(Sort.Direction.DESC, "created");
        Pageable page = PageRequest.of(from, size, sortByCreatedDesc);
        List<ItemRequest> requests = itemRequestRepository.findByRequesterIdNot(page, userId).getContent();

        List<ItemRequestDto> result = new LinkedList<>();
        for (ItemRequest itemRequest : requests) {
            List<ItemDto> itemDtos = ItemMapper.toItemDtoList(itemRepository.findByRequestId(itemRequest.getId()));
            result.add(ItemRequestMapper.toItemRequestDto(itemRequest, itemDtos));
        }
        return result;
    }

    private User getUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }
}
