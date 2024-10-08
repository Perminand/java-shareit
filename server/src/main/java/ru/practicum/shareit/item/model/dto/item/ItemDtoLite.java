package ru.practicum.shareit.item.model.dto.item;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDtoLite {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
