package ru.practicum.shareit;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.CommentFullDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class TestData {
    public static final Long NOT_FOUND_ID = 999L;

    public static final LocalDateTime futureStartTime = LocalDateTime.now().plusHours(1);
    public static final LocalDateTime futureEndTime = LocalDateTime.now().plusHours(2);
    public static final LocalDateTime currentDateTime = LocalDateTime.now();
    public static final LocalDateTime pastStartTime = LocalDateTime.now().minusHours(2);
    public static final LocalDateTime pastEndTime = LocalDateTime.now().minusHours(1);

    public static final User user1 = User.builder().id(1L).name("user 1").email("user1@mail.com").build();
    public static final User user2 = User.builder().id(2L).name("user 2").email("user2@mail.com").build();
    public static final User user3 = User.builder().id(3L).name("user 3").email("user3@mail.com").build();
    public static final User user4 = User.builder().id(4L).name("user 4").email("user4@mail.com").build();
    public static final User user5 = User.builder().id(5L).name("user 5").email("user5@mail.com").build();

    public static final UserDto userDto = UserDto.builder().id(1L).name("test").email("test@mail.com").build();

    public static final ItemRequestDto requestDto = ItemRequestDto.builder()
            .id(1L)
            .description("test")
            .created(currentDateTime)
            .items(List.of(ItemDto.builder()
                    .id(1L)
                    .name("test")
                    .requestId(1L)
                    .available(true)
                    .description("test item")
                    .build()))
            .build();

    public static final Item item1OfUser1 = Item.builder()
            .id(1L).name("item 1").description("description of item 1").owner(user1).available(true).build();
    public static final Item item2OfUser1 = Item.builder()
            .id(2L).name("item 2").description("description of item 2").owner(user1).available(true).build();
    public static final Item item3OfUser2 = Item.builder()
            .id(3L).name("item 3").description("description of item 3").owner(user2).available(true).build();
    public static final Item itemNotAvailable = Item.builder()
            .id(4L).name("item 4").description("not available item").owner(user3).available(false).build();

    public static final Booking booking2WithApprove = Booking.builder()
            .id(2L)
            .start(futureStartTime)
            .end(futureEndTime)
            .status(Status.APPROVED)
            .booker(user2)
            .item(item1OfUser1)
            .build();
    public static final Booking booking5OfItem1 = Booking.builder()
            .id(5L)
            .start(pastStartTime)
            .end(pastEndTime)
            .status(Status.CANCELED)
            .booker(user2)
            .item(item1OfUser1)
            .build();
    public static final Booking booking3WithRejected = Booking.builder()
            .id(3L)
            .start(futureStartTime)
            .end(futureEndTime)
            .status(Status.REJECTED)
            .booker(user2)
            .item(item2OfUser1)
            .build();
    public static final Booking booking4OfItem2 = Booking.builder()
            .id(4L)
            .start(currentDateTime)
            .end(futureEndTime)
            .status(Status.WAITING)
            .booker(user2)
            .item(item2OfUser1)
            .build();
    public static final Booking booking1OfItem3 = Booking.builder()
            .id(1L)
            .start(futureStartTime)
            .end(futureEndTime)
            .status(Status.WAITING)
            .booker(user1)
            .item(item3OfUser2)
            .build();

    public static final CommentFullDto commentFullDto = CommentFullDto.builder()
            .id(1L).text("test comment").authorName("Author").created(currentDateTime).build();

    public static final ItemFullDto itemFullDto = ItemFullDto.builder()
            .id(1L)
            .name("name")
            .description("description")
            .available(true)
            .ownerId(1L)
            .requestId(1L)
            .lastBooking(BookingShortDto.builder().id(1L).bookerId(1L).build())
            .comments(List.of(commentFullDto))
            .build();

    public static final ItemDto itemDto = ItemDto.builder()
            .id(1L).name("test").description("description").available(true).requestId(2L).build();


    public static User getNewUser() {
        return User.builder()
                .name("user")
                .email("test@mail.com")
                .build();
    }

    public static Item getNewItem(User owner) {
        return Item.builder()
                .name("name")
                .description("description")
                .available(true)
                .owner(owner)
                .build();
    }

    public static ItemRequest getNewItemRequest(User requester) {
        return ItemRequest.builder()
                .description("test")
                .requester(requester)
                .created(currentDateTime)
                .build();
    }

    public static Booking getNewBooking(User booker, Item item) {
        return Booking.builder().booker(booker).item(item).status(Status.WAITING)
                .start(currentDateTime).end(futureEndTime).build();
    }
}

