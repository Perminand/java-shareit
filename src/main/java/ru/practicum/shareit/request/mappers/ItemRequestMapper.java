package ru.practicum.shareit.request.mappers;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;

public class ItemRequestMapper {
    public static ItemRequest toItemRequest(ItemRequestDto itemDto) {
        return new ItemRequest(
                itemDto.getId(),
                itemDto.getDescription(),
                itemDto.getRequestor(),
                itemDto.getCreated());
    }
}
