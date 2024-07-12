package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> itemMap = new HashMap<>();
    private final Map<Long, ItemRequest> itemRequestMap = new HashMap<>();
    private long generateId = 0L;
    private long generateIdRequest = 0L;

    @Override
    public List<Item> getItems(long userId) {
        List<Item> itemList = itemMap.values()
                .stream()
                .filter(item -> item.getOwner().getId() == userId)
                .toList();
        return itemList;
    }

    @Override
    public Item create(long userId, Item item) {
        item.setId(++generateId);
        itemMap.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        return itemMap.replace(item.getId(), item);
    }

    @Override
    public List<Item> search(String text) {
        List<Item> itemList = itemMap.values()
                .stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .toList();
        return itemList;
    }

    @Override
    public void createRequest(User user, String text) {
        ItemRequest itemRequest = new ItemRequest(++generateIdRequest, text, user, LocalDateTime.now());
        itemRequestMap.put(++generateIdRequest, itemRequest);
    }

    @Override
    public void deleteByUserIdAndItemId(long userId, long itemId) {
        itemMap.remove(itemId);
    }

    @Override
    public Optional<Item> getById(long itemId) {
        return Optional.of(itemMap.get(itemId));
    }


}
