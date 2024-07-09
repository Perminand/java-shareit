package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDto {
    private long id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    private Boolean available;
}

