package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerId(Long ownerId);

    List<Item> findByOwnerId(Long ownerId, Sort sort);

    @Query(value = "delete from items where owner_id = ?1 and item_request_id=?2", nativeQuery = true)
    void deleteByOwnerAndId(long userId, long itemId);

    List<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}
