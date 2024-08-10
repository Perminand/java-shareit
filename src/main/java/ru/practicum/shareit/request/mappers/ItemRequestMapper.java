package ru.practicum.shareit.request.mappers;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoLite;

public class ItemRequestMapper {
    public static ItemRequest toItemRequest(ItemRequestDto itemDto) {
        return new ItemRequest(
                itemDto.getId(),
                itemDto.getDescription(),
                itemDto.getRequester(),
                itemDto.getCreated());
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(), itemRequest.getDescription(), itemRequest.getRequester(),
                itemRequest.getCreated());
    }

    public static ItemRequestDtoLite toItemRequestDtoLite(ItemRequest itemRequest) {
        return new ItemRequestDtoLite((itemRequest.getDescription()),itemRequest.getCreated());
    }
}
