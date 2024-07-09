package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    List<ItemDto> getItemsByUserId(long userId);

    ItemDto getById(long itemId);

    ItemDto create(Long userId, ItemDto item);

    void deleteItem(long userId, long itemId);

    Item update(Long userId, long itemId, ItemDto itemDto);

    List<ItemDto> search(Long userId, String text);
}
