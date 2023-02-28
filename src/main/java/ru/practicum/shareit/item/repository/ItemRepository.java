package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByIdAndOwnerId(Long itemId, Long userId);

    Optional<Item> findByIdAndOwnerIdNot(Long itemId, Long ownerId);

    List<Item> findByOwnerId(Long userId);

    void deleteByIdAndOwnerId(Long itemId, Long userId);

    @Query("SELECT i FROM Item i WHERE upper(i.name) LIKE upper( concat('%', ?1, '%')) " +
            "OR upper(i.description) LIKE upper( concat('%', ?1, '%')) AND i.available = true")
    List<Item> searchByText(String text);
}
