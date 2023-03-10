package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
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
    void shouldBeFailedAddCommentWithNotBookingItem() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item3OfUser2));
        Mockito.when(bookingRepository.findAllByBookerIdAndItemId(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenReturn(Collections.emptyList());

        CommentCreationDto creationDto = new CommentCreationDto(null, "test");
        Assertions.assertThrows(ValidationException.class, () ->
                itemService.addComment(user1.getId(), item3OfUser2.getId(), creationDto));
    }

    @Test
    void shouldBeFailedWhenBookingTimeNotEnd() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user2));
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item2OfUser1));

        CommentCreationDto creationDto = new CommentCreationDto(null, "test");
        Assertions.assertThrows(ValidationException.class, () ->
                itemService.addComment(user2.getId(), item2OfUser1.getId(), creationDto));
    }
}
