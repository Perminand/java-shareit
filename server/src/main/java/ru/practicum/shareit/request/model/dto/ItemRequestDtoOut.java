package ru.practicum.shareit.request.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.dto.item.ItemDtoLite;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequestDtoOut {
    private Long id;
    private String description;
    private User requester;
    private LocalDateTime created;
    private List<ItemDtoLite> items;
}