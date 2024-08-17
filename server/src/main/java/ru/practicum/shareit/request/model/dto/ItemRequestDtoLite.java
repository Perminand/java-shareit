package ru.practicum.shareit.request.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.dto.item.ItemDtoForAnswer;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@AllArgsConstructor
public class ItemRequestDtoLite {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemDtoForAnswer> ownerList;
}
