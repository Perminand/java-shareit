package ru.practicum.shareit.request;

import ru.practicum.shareit.request.model.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoLite;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoOut;

import java.util.List;


public interface ItemRequestService {

    ItemRequestDtoOut create(Long userId, ItemRequestDto itemRequestDto);

    List<ItemRequestDtoLite> findByUserId(Long userId);

    List<ItemRequestDto> findItemRequestOwnerUser(Long userId, int from, int size);

    ItemRequestDtoOut getItemRequest(Long userId, Long requestId);

}
