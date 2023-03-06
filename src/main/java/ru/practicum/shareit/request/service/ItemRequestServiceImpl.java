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
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

        return getRequestWithItemsDtos(requests);
    }

    @Override
    public ItemRequestDto getById(long userId, long requestId) {
        getUser(userId);

        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));

        List<ItemDto> itemDtos = ItemMapper.toItemDtos(itemRepository.findByRequestId(requestId));
        return ItemRequestMapper.toItemRequestDto(request, itemDtos);
    }

    @Override
    public List<ItemRequestDto> getAll(long userId, int from, int size) {
        getUser(userId);

        Sort sortByCreatedDesc = Sort.by(Sort.Direction.DESC, "created");
        Pageable page = PageRequest.of(from, size, sortByCreatedDesc);
        List<ItemRequest> requests = itemRequestRepository.findByRequesterIdNot(page, userId).getContent();

        return getRequestWithItemsDtos(requests);
    }

    private List<ItemRequestDto> getRequestWithItemsDtos(List<ItemRequest> requests) {
        List<Item> allItems = itemRepository.findByRequestIdIn(requests.stream().map(ItemRequest::getId)
                .collect(Collectors.toList()));

        Map<Long, List<Item>> itemsByRequestId = allItems.stream()
                .collect(Collectors.groupingBy(i -> i.getRequest().getId()));

        List<ItemRequestDto> requestDtos = new LinkedList<>();
        for (ItemRequest itemRequest : requests) {
            List<Item> items = itemsByRequestId.get(itemRequest.getId()) != null
                    ? itemsByRequestId.get(itemRequest.getId())
                    : Collections.emptyList();
            List<ItemDto> itemDtos = ItemMapper.toItemDtos(items);
            requestDtos.add(ItemRequestMapper.toItemRequestDto(itemRequest, itemDtos));
        }

        return requestDtos;
    }

    private User getUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }
}
