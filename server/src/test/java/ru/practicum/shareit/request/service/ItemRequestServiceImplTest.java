package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static ru.practicum.shareit.TestData.NOT_FOUND_ID;
import static ru.practicum.shareit.TestData.user1;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;

    private ItemRequestService requestService;

    @BeforeEach
    void setUp() {
        requestService = new ItemRequestServiceImpl(userRepository, itemRepository, itemRequestRepository);
    }

    @Test
    void shouldBeFailedWhenUserNotFound() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class,
                () -> requestService.create(user1.getId(), new ItemRequestCreationDto(null, "test")));
    }

    @Test
    void shouldBeFailedWhenRequestNotFound() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class,
                () -> requestService.getById(user1.getId(), NOT_FOUND_ID));
    }
}
