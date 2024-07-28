package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.dto.item.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> getItemsByUserId(long userId);

    ItemDto getById(long itemId);

    ItemDto create(Long userId, ItemDto item);

    void deleteItem(long userId, long itemId);

    ItemDto update(Long userId, long itemId, ItemDto itemDto);

    List<ItemDto> search(Long userId, String text);

    Comment createComment(Long userId, Long itemId, Comment comment);

}
