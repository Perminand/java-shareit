package ru.practicum.shareit.item.mappers;


import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInfoDto;
import ru.practicum.shareit.item.model.Item;

public class ItemMapper {
    public static ItemInfoDto itemInfoDto(Item item, Booking lastBooking, Booking nextBooking) {
        ItemInfoDto itemInfoDto = new ItemInfoDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                new ItemInfoDto.BookingDto(lastBooking.getId(), lastBooking.getId()),
                new ItemInfoDto.BookingDto(nextBooking.getId(), nextBooking.getId()));
        return itemInfoDto;
    }

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
