package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.practicum.shareit.TestData.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;

    private ItemService itemService;

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(
                itemRepository, userRepository, bookingRepository, commentRepository, itemRequestRepository);
    }

    @Test
    void shouldBeFailedCreateIfRequestNotFound() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class,
                () -> itemService.create(user1.getId(), ItemDto.builder().requestId(1L).build()));
    }

    @Test
    void shouldBeFailedCreateIfOwnerNotFound() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class,
                () -> itemService.create(user1.getId(), itemDto));
    }

    @Test
    void shouldBeFailedIfGetNotFound() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> itemService.get(user1.getId(), NOT_FOUND_ID));
    }

    @Test
    void shouldBeFailedWhenUpdateNotOwner() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findByIdAndOwnerId(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class,
                () -> itemService.update(user1.getId(), item3OfUser2.getId(), itemDto));
    }

    @Test
    void shouldBeFailedAddCommentWithNotBookingItem() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item3OfUser2));
        Mockito.when(bookingRepository.findAllByBookerIdAndItemId(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenReturn(Collections.emptyList());

        CommentCreationDto creationDto = new CommentCreationDto(null, "test");

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                itemService.addComment(user1.getId(), item3OfUser2.getId(), creationDto));
    }

    @Test
    void shouldBeFailedWhenBookingTimeNotEnd() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user2));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item2OfUser1));
        Mockito.when(bookingRepository.findAllByBookerIdAndItemId(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenReturn(List.of(booking4OfItem2));

        CommentCreationDto creationDto = new CommentCreationDto(null, "test");

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                itemService.addComment(user2.getId(), item2OfUser1.getId(), creationDto));
    }
}
