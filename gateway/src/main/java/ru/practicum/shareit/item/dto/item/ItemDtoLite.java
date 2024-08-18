package ru.practicum.shareit.item.dto.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
@AllArgsConstructor
public class ItemDtoLite {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;

    public ItemDtoLite(String name, String description, Boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
