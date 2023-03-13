package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.practicum.shareit.TestData.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    private final EntityManager em;
    private final BookingService bookingService;

    @Test
    void shouldSaveBooking() {
        User owner = User.builder().name("owner").email("owner@mail.com").build();
        User user = User.builder().name("user").email("user@mail.com").build();
        Item item = getNewItem(owner);

        em.persist(owner);
        em.persist(user);
        em.persist(item);
        BookingFullDto saved = bookingService.create(user.getId(), BookingCreationDto.builder()
                .itemId(item.getId())
                .start(currentDateTime)
                .end(futureEndTime)
                .build());
        Booking fromDb = em.createQuery("SELECT b FROM Booking b WHERE b.id = :bookingId", Booking.class)
                .setParameter("bookingId", saved.getId())
                .getSingleResult();

        assertThat(fromDb.getId(), notNullValue());
        assertThat(fromDb.getItem(), equalTo(item));
        assertThat(fromDb.getStart(), equalTo(currentDateTime));
        assertThat(fromDb.getEnd(), equalTo(futureEndTime));
        assertThat(fromDb.getStatus(), equalTo(Status.WAITING));
        assertThat(fromDb.getBooker(), equalTo(user));
    }

    @Test
    void shouldApproveStatus() {
        User owner = User.builder().name("owner").email("owner@mail.com").build();
        User booker = User.builder().name("user").email("user@mail.com").build();
        Item item = getNewItem(owner);
        Booking booking = getNewBooking(booker, item);

        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);
        bookingService.approveStatus(owner.getId(), booking.getId(), true);
        Booking fromDb = em.createQuery("SELECT b FROM Booking b WHERE b.id = :bookingId", Booking.class)
                .setParameter("bookingId", booking.getId())
                .getSingleResult();

        assertThat(fromDb.getStatus(), equalTo(Status.APPROVED));
    }

    @Test
    void shouldFindById() {
        User owner = User.builder().name("owner").email("owner@mail.com").build();
        User booker = User.builder().name("user").email("user@mail.com").build();
        Item item = getNewItem(owner);
        Booking booking = getNewBooking(booker, item);

        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);
        BookingFullDto fromDb = bookingService.get(booker.getId(), booking.getId());
        BookingFullDto fullDto = BookingMapper.toBookingFullDto(booking);

        assertThat(fromDb, equalTo(fullDto));
    }

    @Test
    void shouldFindByState() {
        User owner = User.builder().name("owner").email("owner@mail.com").build();
        User booker = User.builder().name("user").email("user@mail.com").build();
        Item item = getNewItem(owner);
        Booking booking = getNewBooking(booker, item);

        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);
        Collection<BookingFullDto> all = bookingService.getAllByState(booker.getId(), State.ALL, 0, 1);
        BookingFullDto fullDto = BookingMapper.toBookingFullDto(booking);

        assertThat(all, hasSize(1));
        assertThat(all, hasItem(fullDto));
    }

    @Test
    void shouldFindByOwnerAndState() {
        User owner = User.builder().name("owner").email("owner@mail.com").build();
        User booker = User.builder().name("user").email("user@mail.com").build();
        Item item = getNewItem(owner);
        Booking booking = getNewBooking(booker, item);

        em.persist(owner);
        em.persist(booker);
        em.persist(item);
        em.persist(booking);
        Collection<BookingFullDto> all = bookingService.getAllByOwnerAndState(owner.getId(), State.ALL, 0, 1);
        BookingFullDto fullDto = BookingMapper.toBookingFullDto(booking);

        assertThat(all, hasSize(1));
        assertThat(all, hasItem(fullDto));
    }
}
