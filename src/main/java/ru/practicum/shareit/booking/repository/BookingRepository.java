package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Collection<Booking> findAllByBookerId(long bookerId, Sort sort);

    Collection<Booking> findAllByItemIdIn(Collection<Long> itemIds, Sort sort);

    Collection<Booking> findAllByItemId(Long itemId, Sort sort);

    List<Booking> findAllByBookerIdAndItemId(Long bookerId, Long itemId, Sort sort);
}
