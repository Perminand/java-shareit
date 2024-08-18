package ru.practicum.shareit.request.mappers;

import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.dto.item.ItemDtoLite;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoLite;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoOut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ItemRequestMapper {
    public static ItemRequest toItemRequest(ItemRequestDto itemDto) {
        return ItemRequest.builder()
                .description(itemDto.getDescription())
                .build();
    }

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(), itemRequest.getDescription(), itemRequest.getRequester(),
                itemRequest.getCreated());
    }

    public static ItemRequestDtoLite toItemRequestDtoLite(ItemRequest itemRequest) {
        return new ItemRequestDtoLite(itemRequest.getId(), itemRequest.getDescription(), itemRequest.getCreated(), null);
    }

    public static ItemRequestDtoOut toRequestDtoOut(ItemRequest request) {
        List<ItemDtoLite> itemsDtoOut = new ArrayList<>();
        if (!Objects.isNull(request.getItems())) {
            itemsDtoOut = request.getItems().stream()
                    .map(ItemMapper::toItemDtoLite)
                    .collect(Collectors.toList());
        }
        return ItemRequestDtoOut.builder()
                .id(request.getId())
                .description(request.getDescription())
                .created(request.getCreated())
                .items(itemsDtoOut)
                .build();
    }
}
