package ru.practicum.shareit.item.mappers;


import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.state.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.comment.CommentDto;
import ru.practicum.shareit.item.model.dto.item.ItemDto;
import ru.practicum.shareit.item.model.dto.item.ItemDtoLite;
import ru.practicum.shareit.item.model.dto.item.ItemInfoDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static ru.practicum.shareit.booking.mappers.BookingMapper.toBookingLiteDto;

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
        ItemDto itemdto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(new ArrayList<>())
                .build();
        return itemdto;
    }

    public static ItemDtoLite toItemDtoLite(Item item) {
        return ItemDtoLite.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getName(),
                itemDto.getDescription());

    }

    public static ItemDto toItemDtoWithBookingsAndComments(Item item, List<BookingDto> bookings, List<CommentDto> comments) {
        ItemDto itemDto = null;
        if (bookings == null) {
            itemDto = toItemDto(item);
        } else {
            itemDto = toItemDtoWithBookings(item, bookings);
        }
        itemDto.setComments(comments);
        return itemDto;
    }

    public static ItemDto toItemDtoWithComments(Item item, List<CommentDto> comments) {
        ItemDto itemDto = toItemDto(item);
        itemDto.setComments(comments);
        return itemDto;
    }

    public static ItemDto toItemDtoWithBookings(Item item, List<BookingDto> bookings) {
        BookingDto lastBooking = null;
        BookingDto nextBooking = null;
        if (!bookings.isEmpty()) {
            lastBooking = bookings.stream()
                    .filter(x -> x.getStatus() != BookingStatus.REJECTED)
                    .filter(x -> x.getStatus() != BookingStatus.CANCELED)
                    .filter(x -> x.getStart().isBefore(LocalDateTime.now()))
                    .max(Comparator.comparing(BookingDto::getStart)).orElse(null);
            nextBooking = bookings.stream()
                    .filter(x -> x.getStatus() != BookingStatus.REJECTED)
                    .filter(x -> x.getStatus() != BookingStatus.CANCELED)
                    .filter(x -> x.getStart().isAfter(LocalDateTime.now()))
                    .min(Comparator.comparing(BookingDto::getStart)).orElse(null);
        }
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(toBookingLiteDto(lastBooking))
                .nextBooking(toBookingLiteDto(nextBooking))
                .comments(new ArrayList<>())
                .build();
    }
}
