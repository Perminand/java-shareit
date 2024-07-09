package ru.practicum.shareit.item.mappers;


import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable());
    }

    public static Item forItem(ItemDto itemDto) {
        return new Item(
                itemDto.getName(),
                itemDto.getDescription());

    }
}