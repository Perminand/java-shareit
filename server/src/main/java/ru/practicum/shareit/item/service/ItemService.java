package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.dto.comment.CommentDto;
import ru.practicum.shareit.item.model.dto.comment.CommentDtoOut;
import ru.practicum.shareit.item.model.dto.item.ItemDto;
import ru.practicum.shareit.item.model.dto.item.ItemDtoLite;

import java.util.List;

public interface ItemService {

    List<ItemDto> getItemsByUserId(long userId);

    ItemDto create(Long userId, ItemDto item);

    CommentDtoOut createComment(Long userId, Long itemId, CommentDto commentDto);

    void deleteItem(long userId, long itemId);

    ItemDtoLite update(Long userId, long itemId, ItemDto itemDto);

    List<ItemDto> search(Long userId, String text);


    ItemDto getItemById(long itemId, long userId);

}
