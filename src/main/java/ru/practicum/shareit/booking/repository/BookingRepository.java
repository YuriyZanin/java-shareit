package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerId(long bookerId, Sort sort);

    List<Booking> findAllByItemIdAndStatus(Long itemId, Status status);

    List<Booking> findAllByItemIdInAndStatus(List<Long> ids, Status status);

    @Query("SELECT b FROM Booking b " +
            "INNER JOIN Item i ON i.id = b.item.id " +
            "WHERE i.owner.id = :ownerId " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwner(Long ownerId);

    List<Booking> findAllByBookerIdAndItemId(Long bookerId, Long itemId, Sort sort);
}
