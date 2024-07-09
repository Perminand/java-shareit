package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    List<Item> getItems(long userId);

    Item create(long userId, Item item);

    void deleteByUserIdAndItemId(long userId, long itemId);

    Optional<Item> getById(long itemId);

    Item update(Item item);

    List<Item> search(String text);

    void createRequest(User user, String text);
}
