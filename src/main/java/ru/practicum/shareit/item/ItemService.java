package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.dto.comment.CommentDto;
import ru.practicum.shareit.item.model.dto.item.ItemDto;
import ru.practicum.shareit.item.model.dto.item.ItemDtoLite;

import java.util.List;

public interface ItemService {

    List<ItemDto> getItemsByUserId(long userId);

    ItemDto getById(long itemId);

    ItemDtoLite create(Long userId, ItemDto item);

    void deleteItem(long userId, long itemId);

    ItemDtoLite update(Long userId, long itemId, ItemDto itemDto);

    List<ItemDto> search(Long userId, String text);

    CommentDto createComment(Long userId, Long itemId, CommentDto commentDto);

    ItemDto getItemById(long itemId, long userId);

}
