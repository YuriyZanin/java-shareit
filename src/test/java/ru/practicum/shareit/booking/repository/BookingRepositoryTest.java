package ru.practicum.shareit.booking.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.AbstractRepositoryTest;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

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
        Item item = TestData.getNewItem(owner);
        Booking booking = TestData.getNewBooking(booker, item);

        entityManager.persist(booker);
        entityManager.persist(owner);
        entityManager.persist(item);
        Booking saved = bookingRepository.save(booking);
        Booking fromDb = entityManager.getEntityManager()
                .createQuery("SELECT b FROM Booking b WHERE b.id = :bookingId", Booking.class)
                .setParameter("bookingId", saved.getId())
                .getSingleResult();

        assertThat(fromDb.getId(), notNullValue());
        assertThat(fromDb.getItem(), equalTo(item));
        assertThat(fromDb.getBooker(), equalTo(booker));
        assertThat(fromDb.getStart(), equalTo(booking.getStart()));
        assertThat(fromDb.getEnd(), equalTo(booking.getEnd()));
        assertThat(fromDb.getStatus(), equalTo(Status.WAITING));
    }

    @Test
    void shouldFindByBooker() {
        User booker = User.builder().name("name").email("test@mail.com").build();
        User owner = User.builder().name("owner").email("owner@mail.com").build();
        Item item = TestData.getNewItem(owner);
        Booking booking = TestData.getNewBooking(booker, item);

        entityManager.persist(booker);
        entityManager.persist(owner);
        entityManager.persist(item);
        entityManager.persist(booking);
        Sort start = Sort.by("start");
        Pageable pageable = PageRequest.of(0, 3, start);
        List<Booking> all = bookingRepository.findAllByBookerId(booker.getId(), pageable);

        assertThat(all, not(empty()));
        assertThat(all, hasSize(1));
        assertThat(all, hasItem(booking));
    }

    @Test
    void shouldFindByItemAndStatus() {
        User booker = User.builder().name("name").email("test@mail.com").build();
        User owner = User.builder().name("owner").email("owner@mail.com").build();
        Item item = TestData.getNewItem(owner);
        Booking booking = TestData.getNewBooking(booker, item);

        entityManager.persist(booker);
        entityManager.persist(owner);
        entityManager.persist(item);
        entityManager.persist(booking);
        List<Booking> bookings = bookingRepository.findAllByItemIdAndStatus(item.getId(), Status.WAITING);

        assertThat(bookings, hasSize(1));
        assertThat(bookings, hasItem(booking));
    }

    @Test
    void shouldFindByItemsAndStatus() {
        User booker = User.builder().name("name").email("test@mail.com").build();
        User owner = User.builder().name("owner").email("owner@mail.com").build();
        Item item = TestData.getNewItem(owner);
        Booking booking = TestData.getNewBooking(booker, item);

        entityManager.persist(booker);
        entityManager.persist(owner);
        entityManager.persist(item);
        entityManager.persist(booking);
        List<Booking> bookings = bookingRepository.findAllByItemIdInAndStatus(List.of(item.getId()), Status.WAITING);

        assertThat(bookings, hasSize(1));
        assertThat(bookings, hasItem(booking));
    }

    @Test
    void shouldFindByOwner() {
        User booker = User.builder().name("name").email("test@mail.com").build();
        User owner = User.builder().name("owner").email("owner@mail.com").build();
        Item item = TestData.getNewItem(owner);
        Booking booking = TestData.getNewBooking(booker, item);

        entityManager.persist(booker);
        entityManager.persist(owner);
        entityManager.persist(item);
        entityManager.persist(booking);
        Pageable pageable = PageRequest.of(0, 3);
        List<Booking> all = bookingRepository.findAllByOwnerId(owner.getId(), pageable);

        assertThat(all, not(empty()));
        assertThat(all, hasSize(1));
        assertThat(all, hasItem(booking));
    }

    @Test
    void shouldFindByBookerAndItem() {
        User booker = User.builder().name("name").email("test@mail.com").build();
        User owner = User.builder().name("owner").email("owner@mail.com").build();
        Item item = TestData.getNewItem(owner);
        Booking booking = TestData.getNewBooking(booker, item);

        entityManager.persist(booker);
        entityManager.persist(owner);
        entityManager.persist(item);
        entityManager.persist(booking);
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndItemId(
                booker.getId(), item.getId(), Sort.by("start"));

        assertThat(bookings, hasSize(1));
        assertThat(bookings, hasItem(booking));
    }
}
