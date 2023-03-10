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
import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {
    private final EntityManager em;
    private final BookingService bookingService;

    @Test
    void shouldSaveBooking() {
        User owner = User.builder().name("owner").email("owner@mail.com").build();
        em.persist(owner);

        User user = User.builder().name("user").email("user@mail.com").build();
        em.persist(user);

        Item item = Item.builder().owner(owner).available(true).name("name").description("desc").build();
        em.persist(item);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        BookingCreationDto dto = BookingCreationDto.builder().itemId(item.getId()).start(start).end(end).build();
        BookingFullDto saved = bookingService.create(user.getId(), dto);

        Booking fromDb = em.createQuery("SELECT b FROM Booking b WHERE b.id = :bookingId", Booking.class)
                .setParameter("bookingId", saved.getId())
                .getSingleResult();
        assertThat(fromDb.getId(), notNullValue());
        assertThat(fromDb.getItem(), equalTo(item));
        assertThat(fromDb.getStart(), equalTo(start));
        assertThat(fromDb.getEnd(), equalTo(end));
        assertThat(fromDb.getStatus(), equalTo(Status.WAITING));
        assertThat(fromDb.getBooker(), equalTo(user));
    }

    @Test
    void shouldApproveStatus() {
        User owner = User.builder().name("owner").email("owner@mail.com").build();
        em.persist(owner);

        User user = User.builder().name("user").email("user@mail.com").build();
        em.persist(user);

        Item item = Item.builder().owner(owner).available(true).name("name").description("desc").build();
        em.persist(item);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        Booking booking = Booking.builder().booker(user).item(item).status(Status.WAITING).start(start).end(end).build();
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
        em.persist(owner);

        User user = User.builder().name("user").email("user@mail.com").build();
        em.persist(user);

        Item item = Item.builder().owner(owner).available(true).name("name").description("desc").build();
        em.persist(item);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        Booking booking = Booking.builder().booker(user).item(item).status(Status.WAITING).start(start).end(end).build();
        em.persist(booking);

        BookingFullDto fromDb = bookingService.get(user.getId(), booking.getId());
        BookingFullDto fullDto = BookingMapper.toBookingFullDto(booking);
        assertThat(fromDb, equalTo(fullDto));
    }

    @Test
    void shouldFindByState() {
        User owner = User.builder().name("owner").email("owner@mail.com").build();
        em.persist(owner);

        User user = User.builder().name("user").email("user@mail.com").build();
        em.persist(user);

        Item item = Item.builder().owner(owner).available(true).name("name").description("desc").build();
        em.persist(item);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        Booking booking = Booking.builder().booker(user).item(item).status(Status.WAITING).start(start).end(end).build();
        em.persist(booking);

        Collection<BookingFullDto> all = bookingService.getAllByState(user.getId(), State.ALL, 0, 1);
        BookingFullDto fullDto = BookingMapper.toBookingFullDto(booking);
        assertThat(all, hasSize(1));
        assertThat(all, hasItem(fullDto));
    }

    @Test
    void shouldFindByOwnerAndState() {
        User owner = User.builder().name("owner").email("owner@mail.com").build();
        em.persist(owner);

        User user = User.builder().name("user").email("user@mail.com").build();
        em.persist(user);

        Item item = Item.builder().owner(owner).available(true).name("name").description("desc").build();
        em.persist(item);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        Booking booking = Booking.builder().booker(user).item(item).status(Status.WAITING).start(start).end(end).build();
        em.persist(booking);

        Collection<BookingFullDto> all = bookingService.getAllByOwnerAndState(owner.getId(), State.ALL, 0, 1);
        BookingFullDto fullDto = BookingMapper.toBookingFullDto(booking);
        assertThat(all, hasSize(1));
        assertThat(all, hasItem(fullDto));
    }
}
