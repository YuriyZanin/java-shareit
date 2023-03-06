package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findAllByBookerId(Pageable page, long bookerId);

    List<Booking> findAllByItemIdAndStatus(Long itemId, Status status);

    List<Booking> findAllByItemIdInAndStatus(List<Long> ids, Status status);

    @Query("SELECT b FROM Booking b " +
            "INNER JOIN Item i ON i.id = b.item.id " +
            "WHERE i.owner.id = :ownerId " +
            "ORDER BY b.start DESC")
    Page<Booking> findAllByOwner(Pageable page, Long ownerId);

    List<Booking> findAllByBookerIdAndItemId(Long bookerId, Long itemId, Sort sort);
}
