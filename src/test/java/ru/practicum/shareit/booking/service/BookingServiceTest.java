package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.practicum.shareit.TestData.futureEndTime;
import static ru.practicum.shareit.TestData.futureStartTime;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class BookingServiceTest {
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;

    @Test
    void shouldSaveAndGetBookings() {
        UserDto owner = UserDto.builder().name("owner").email("owner@mail.com").build();
        UserDto user = UserDto.builder().name("user").email("user@mail.com").build();
        ItemDto item1 = ItemDto.builder().name("item1").description("item 1").available(true).build();
        ItemDto item2 = ItemDto.builder().name("item2").description("item 2").available(true).build();
        ItemDto item3 = ItemDto.builder().name("item3").description("item 3").available(true).build();

        UserDto createdOwner = userService.create(owner);
        ItemFullDto createdItem1 = itemService.create(createdOwner.getId(), item1);
        ItemFullDto createdItem2 = itemService.create(createdOwner.getId(), item2);
        ItemFullDto createdItem3 = itemService.create(createdOwner.getId(), item3);
        UserDto createdUser = userService.create(user);

        BookingCreationDto booking1Dto = BookingCreationDto.builder()
                .itemId(createdItem1.getId()).start(futureStartTime).end(futureEndTime).build();
        BookingCreationDto booking2Dto = BookingCreationDto.builder()
                .itemId(createdItem2.getId()).start(futureStartTime).end(futureEndTime).build();
        BookingCreationDto booking3Dto = BookingCreationDto.builder()
                .itemId(createdItem3.getId()).start(futureStartTime).end(futureEndTime).build();

        BookingFullDto createdBooking1 = bookingService.create(createdUser.getId(), booking1Dto);
        BookingFullDto createdBooking2 = bookingService.create(createdUser.getId(), booking2Dto);
        BookingFullDto createdBooking3 = bookingService.create(createdUser.getId(), booking3Dto);

        BookingFullDto booking1FromDb = bookingService.get(createdUser.getId(), createdBooking1.getId());
        BookingFullDto booking2FromDb = bookingService.get(createdUser.getId(), createdBooking2.getId());
        BookingFullDto booking3FromDb = bookingService.get(createdUser.getId(), createdBooking3.getId());

        Collection<BookingFullDto> allWaitingByBooker = bookingService.getAllByState(createdUser.getId(),
                State.WAITING, 0, 3);
        Collection<BookingFullDto> allWaitingByOwner = bookingService.getAllByOwnerAndState(createdOwner.getId(),
                State.WAITING, 0, 3);

        BookingFullDto approvedBooking1 = bookingService.approveStatus(createdOwner.getId(),
                createdBooking1.getId(), true);
        BookingFullDto approvedBooking2 = bookingService.approveStatus(createdOwner.getId(),
                createdBooking2.getId(), true);
        BookingFullDto approvedBooking3 = bookingService.approveStatus(createdOwner.getId(),
                createdBooking3.getId(), true);

        Collection<BookingFullDto> allFutureByBooker = bookingService.getAllByState(createdUser.getId(),
                State.FUTURE, 0, 3);
        Collection<BookingFullDto> allFutureByOwner = bookingService.getAllByOwnerAndState(createdOwner.getId(),
                State.FUTURE, 0, 3);

        BookingFullDto rejectedBooking1 = bookingService.approveStatus(createdOwner.getId(),
                createdBooking1.getId(), false);
        BookingFullDto rejectedBooking2 = bookingService.approveStatus(createdOwner.getId(),
                createdBooking2.getId(), false);
        BookingFullDto rejectedBooking3 = bookingService.approveStatus(createdOwner.getId(),
                createdBooking3.getId(), false);

        Collection<BookingFullDto> allRejectedByBooker = bookingService.getAllByState(createdUser.getId(),
                State.REJECTED, 0, 3);
        Collection<BookingFullDto> allRejectedByOwner = bookingService.getAllByOwnerAndState(createdOwner.getId(),
                State.REJECTED, 0, 3);

        assertThat(createdBooking1, equalTo(booking1FromDb));
        assertThat(createdBooking2, equalTo(booking2FromDb));
        assertThat(createdBooking3, equalTo(booking3FromDb));
        assertThat(allWaitingByBooker, not(empty()));
        assertThat(allWaitingByBooker, hasSize(3));
        assertThat(allWaitingByBooker, hasItems(createdBooking1, createdBooking2, createdBooking3));
        assertThat(allWaitingByOwner, not(empty()));
        assertThat(allWaitingByOwner, hasSize(3));
        assertThat(allWaitingByOwner, hasItems(createdBooking1, createdBooking2, createdBooking3));
        assertThat(allFutureByBooker, not(empty()));
        assertThat(allFutureByBooker, hasSize(3));
        assertThat(allFutureByBooker, hasItems(approvedBooking1, approvedBooking2, approvedBooking3));
        assertThat(allFutureByOwner, not(empty()));
        assertThat(allFutureByOwner, hasSize(3));
        assertThat(allFutureByOwner, hasItems(approvedBooking1, approvedBooking2, approvedBooking3));
        assertThat(allRejectedByBooker, not(empty()));
        assertThat(allRejectedByBooker, hasSize(3));
        assertThat(allRejectedByBooker, hasItems(rejectedBooking1, rejectedBooking2, rejectedBooking3));
        assertThat(allRejectedByOwner, not(empty()));
        assertThat(allRejectedByOwner, hasSize(3));
        assertThat(allRejectedByOwner, hasItems(rejectedBooking1, rejectedBooking2, rejectedBooking3));
    }
}
