package ru.practicum.shareit.item.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    private Boolean available;
}

