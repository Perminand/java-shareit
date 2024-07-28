package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwner_Id(Long ownerId);

    @Query(value = "select * from items where items.owner_id=?1", nativeQuery = true)
    List<Item> getItemsByOwner(long userId);

    @Query(value = "delete from items where owner_id = ?1 and item_request_id=?2", nativeQuery = true)
    void deleteByOwnerAndId(long userId, long itemId);

    List<Item> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}
