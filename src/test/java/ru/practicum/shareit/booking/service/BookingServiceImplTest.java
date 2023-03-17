package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static ru.practicum.shareit.TestData.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;

    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
    }

    @Test
    void shouldBeFailedApprovingWhenUserNotOwner() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking1OfItem3));

        Assertions.assertThrows(NotFoundException.class, () ->
                bookingService.approveStatus(user1.getId(), booking1OfItem3.getId(), true));
    }

    @Test
    void shouldBeFailedWhenApprovingTwice() {
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking2WithApprove));
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));

        Assertions.assertThrows(ValidationException.class, () ->
                bookingService.approveStatus(user1.getId(), booking2WithApprove.getId(), true));
    }

    @Test
    void shouldBeFailedWhenRejectedTwice() {
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking3WithRejected));
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));

        Assertions.assertThrows(ValidationException.class, () ->
                bookingService.approveStatus(user1.getId(), booking3WithRejected.getId(), false));
    }

    @Test
    void shouldBeFailedCreateWhenItemNotAvailable() {
        BookingCreationDto creationDto = BookingCreationDto.builder()
                .itemId(itemNotAvailable.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusHours(1))
                .build();

        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(itemRepository.findByIdAndOwnerIdNot(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(Optional.of(itemNotAvailable));

        Assertions.assertThrows(ValidationException.class, () ->
                bookingService.create(user1.getId(), creationDto));
    }

    @Test
    void shouldBeFailedGetWhenUserNeitherBookerNorOwner() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user4));
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking1OfItem3));

        Assertions.assertThrows(NotFoundException.class, () ->
                bookingService.get(user4.getId(), booking1OfItem3.getId()));
    }

    @Test
    void shouldFindByState() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user2));
        Mockito.when(bookingRepository.findAllByBookerId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(List.of(booking2WithApprove, booking3WithRejected));

        Collection<BookingFullDto> all = bookingService.getAllByState(user2.getId(), State.ALL, 0, 2);

        assertThat(all, hasSize(2));
        assertThat(all, hasItem(BookingMapper.toBookingFullDto(booking2WithApprove)));
        assertThat(all, hasItem(BookingMapper.toBookingFullDto(booking3WithRejected)));
    }

    @Test
    void shouldFindAllRejected() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user2));
        Mockito.when(bookingRepository.findAllRejectedByBookerId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(List.of(booking3WithRejected));

        Collection<BookingFullDto> all = bookingService.getAllByState(user2.getId(), State.REJECTED, 0, 2);

        assertThat(all, hasSize(1));
        assertThat(all, hasItem(BookingMapper.toBookingFullDto(booking3WithRejected)));
    }

    @Test
    void shouldFindAllFuture() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user2));
        Mockito.when(bookingRepository.findAllFutureByBookerId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(List.of(booking2WithApprove, booking3WithRejected));

        Collection<BookingFullDto> all = bookingService.getAllByState(user2.getId(), State.FUTURE, 0, 2);

        assertThat(all, hasSize(2));
        assertThat(all, hasItem(BookingMapper.toBookingFullDto(booking2WithApprove)));
        assertThat(all, hasItem(BookingMapper.toBookingFullDto(booking3WithRejected)));
    }

    @Test
    void shouldFindAllWaiting() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user2));
        Mockito.when(bookingRepository.findAllWaitingByBookerId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(List.of(booking4OfItem2));

        Collection<BookingFullDto> all = bookingService.getAllByState(user2.getId(), State.WAITING, 0, 2);

        assertThat(all, hasSize(1));
        assertThat(all, hasItem(BookingMapper.toBookingFullDto(booking4OfItem2)));
    }

    @Test
    void shouldFindAllCurrent() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user2));
        Mockito.when(bookingRepository.findAllCurrentByBookerId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(List.of(booking4OfItem2));

        Collection<BookingFullDto> all = bookingService.getAllByState(user2.getId(), State.CURRENT, 0, 2);

        assertThat(all, hasSize(1));
        assertThat(all, hasItem(BookingMapper.toBookingFullDto(booking4OfItem2)));
    }

    @Test
    void shouldFindAllPast() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user2));
        Mockito.when(bookingRepository.findAllPastByBookerId(Mockito.anyLong(), Mockito.any()))
                .thenReturn(List.of(booking5OfItem1));

        Collection<BookingFullDto> all = bookingService.getAllByState(user2.getId(), State.PAST, 0, 2);

        assertThat(all, hasSize(1));
        assertThat(all, hasItem(BookingMapper.toBookingFullDto(booking5OfItem1)));
    }
}
