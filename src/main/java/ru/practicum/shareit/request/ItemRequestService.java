package ru.practicum.shareit.request;

import ru.practicum.shareit.request.model.dto.ItemRequestDto;


public interface ItemRequestService {

    ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto);

}
