package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerId(long bookerId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId AND CURRENT_TIMESTAMP BETWEEN b.start AND b.end " +
            "ORDER BY b.start DESC")
    List<Booking> findAllCurrentByBookerId(Long bookerId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId AND CURRENT_TIMESTAMP > b.end " +
            "ORDER BY b.start DESC")
    List<Booking> findAllPastByBookerId(Long bookerId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId AND CURRENT_TIMESTAMP < b.start " +
            "ORDER BY b.start DESC")
    List<Booking> findAllFutureByBookerId(Long bookerId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId AND b.status = 'WAITING' " +
            "ORDER BY b.start DESC")
    List<Booking> findAllWaitingByBookerId(Long bookerId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "WHERE b.booker.id = :bookerId AND b.status = 'REJECTED' " +
            "ORDER BY b.start DESC")
    List<Booking> findAllRejectedByBookerId(Long bookerId, Pageable pageable);

    List<Booking> findAllByItemIdAndStatus(Long itemId, Status status);

    List<Booking> findAllByItemIdInAndStatus(List<Long> ids, Status status);

    @Query("SELECT b FROM Booking b " +
            "INNER JOIN Item i ON i.id = b.item.id " +
            "WHERE i.owner.id = :ownerId " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerId(Long ownerId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "JOIN Item i ON b.item.id = i.id " +
            "WHERE i.owner.id = :ownerId AND CURRENT_TIMESTAMP > b.end " +
            "ORDER BY b.start DESC")
    List<Booking> findAllPastByOwnerId(Long ownerId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "JOIN Item i ON b.item.id = i.id " +
            "WHERE i.owner.id = :ownerId AND CURRENT_TIMESTAMP BETWEEN b.start AND b.end " +
            "ORDER BY b.start DESC")
    List<Booking> findAllCurrentByOwnerId(Long ownerId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "JOIN Item i ON b.item.id = i.id " +
            "WHERE i.owner.id = :ownerId AND CURRENT_TIMESTAMP < b.start " +
            "ORDER BY b.start DESC")
    List<Booking> findAllFutureByOwnerId(Long ownerId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "JOIN Item i ON b.item.id = i.id " +
            "WHERE i.owner.id = :ownerId AND b.status = 'WAITING'" +
            "ORDER BY b.start DESC")
    List<Booking> findAllWaitingByOwnerId(Long ownerId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "JOIN Item i ON b.item.id = i.id " +
            "WHERE i.owner.id = :ownerId AND b.status = 'REJECTED' " +
            "ORDER BY b.start DESC")
    List<Booking> findAllRejectedByOwnerId(Long ownerId, Pageable pageable);

    List<Booking> findAllByBookerIdAndItemId(Long bookerId, Long itemId, Sort sort);
}
