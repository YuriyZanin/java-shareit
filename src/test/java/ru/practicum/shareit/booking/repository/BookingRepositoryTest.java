package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.AbstractRepositoryTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class BookingRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void shouldSaveBooking() {
        User booker = User.builder().name("name").email("test@mail.com").build();
        User owner = User.builder().name("owner").email("owner@mail.com").build();
        entityManager.persist(booker);
        entityManager.persist(owner);

        Item item = Item.builder().name("item").description("description").available(true).owner(owner).build();
        entityManager.persist(item);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        Booking booking = bookingRepository.save(Booking.builder()
                .booker(booker)
                .item(item)
                .start(start)
                .end(end)
                .status(Status.WAITING)
                .build());

        assertThat(booking.getId(), notNullValue());
        assertThat(booking.getItem(), equalTo(item));
        assertThat(booking.getBooker(), equalTo(booker));
        assertThat(booking.getStart(), equalTo(start));
        assertThat(booking.getEnd(), equalTo(end));
        assertThat(booking.getStatus(), equalTo(Status.WAITING));
    }

    @Test
    void shouldFindByBooker() {
        User booker = User.builder().name("name").email("test@mail.com").build();
        User owner = User.builder().name("owner").email("owner@mail.com").build();
        entityManager.persist(booker);
        entityManager.persist(owner);

        Item item = Item.builder().name("item").description("description").available(true).owner(owner).build();
        entityManager.persist(item);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .start(start)
                .end(end)
                .status(Status.WAITING)
                .build();
        entityManager.persist(booking);

        Page<Booking> page = bookingRepository.findAllByBookerId(PageRequest.of(0, 1), booker.getId());
        assertThat(page, notNullValue());
        assertThat(page.getContent(), hasSize(1));
        assertThat(page.getContent(), hasItem(booking));
    }

    @Test
    void shouldFindByItemAndStatus() {
        User booker = User.builder().name("name").email("test@mail.com").build();
        User owner = User.builder().name("owner").email("owner@mail.com").build();
        entityManager.persist(booker);
        entityManager.persist(owner);

        Item item = Item.builder().name("item").description("description").available(true).owner(owner).build();
        entityManager.persist(item);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .start(start)
                .end(end)
                .status(Status.WAITING)
                .build();
        entityManager.persist(booking);

        List<Booking> bookings = bookingRepository.findAllByItemIdAndStatus(item.getId(), Status.WAITING);
        assertThat(bookings, hasSize(1));
        assertThat(bookings, hasItem(booking));
    }

    @Test
    void shouldFindByItemsAndStatus() {
        User booker = User.builder().name("name").email("test@mail.com").build();
        User owner = User.builder().name("owner").email("owner@mail.com").build();
        entityManager.persist(booker);
        entityManager.persist(owner);

        Item item = Item.builder().name("item").description("description").available(true).owner(owner).build();
        entityManager.persist(item);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .start(start)
                .end(end)
                .status(Status.WAITING)
                .build();
        entityManager.persist(booking);

        List<Booking> bookings = bookingRepository.findAllByItemIdInAndStatus(List.of(item.getId()), Status.WAITING);
        assertThat(bookings, hasSize(1));
        assertThat(bookings, hasItem(booking));
    }

    @Test
    void shouldFindByOwner() {
        User booker = User.builder().name("name").email("test@mail.com").build();
        User owner = User.builder().name("owner").email("owner@mail.com").build();
        entityManager.persist(booker);
        entityManager.persist(owner);

        Item item = Item.builder().name("item").description("description").available(true).owner(owner).build();
        entityManager.persist(item);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .start(start)
                .end(end)
                .status(Status.WAITING)
                .build();
        entityManager.persist(booking);

        Page<Booking> page = bookingRepository.findAllByOwner(PageRequest.of(0, 1), owner.getId());
        assertThat(page, notNullValue());
        assertThat(page.getContent(), hasSize(1));
        assertThat(page.getContent(), hasItem(booking));
    }

    @Test
    void shouldFindByBookerAndItem() {
        User booker = User.builder().name("name").email("test@mail.com").build();
        User owner = User.builder().name("owner").email("owner@mail.com").build();
        entityManager.persist(booker);
        entityManager.persist(owner);

        Item item = Item.builder().name("item").description("description").available(true).owner(owner).build();
        entityManager.persist(item);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        Booking booking = Booking.builder()
                .booker(booker)
                .item(item)
                .start(start)
                .end(end)
                .status(Status.WAITING)
                .build();
        entityManager.persist(booking);

        List<Booking> bookings = bookingRepository.findAllByBookerIdAndItemId(
                booker.getId(), item.getId(), Sort.by("start"));
        assertThat(bookings, hasSize(1));
        assertThat(bookings, hasItem(booking));
    }
}
