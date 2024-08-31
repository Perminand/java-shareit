package ru.practicum.shareit.booking.mappers;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.dto.BookingLiteDto;
import ru.practicum.shareit.item.model.dto.item.ItemDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;

import static ru.practicum.shareit.item.mappers.ItemMapper.toItemDto;
import static ru.practicum.shareit.user.mappers.UserMapper.toUser;
import static ru.practicum.shareit.user.mappers.UserMapper.toUserDto;

public class BookingMapper {

    public static BookingDtoOut toBookingDto(Booking booking) {
        BookingDtoOut bookingDtoOut = BookingDtoOut.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .build();
        if (booking.getItem() != null) {
            ItemDto itemDto = toItemDto(booking.getItem());
            bookingDtoOut.setItem(itemDto);
            bookingDtoOut.setItemId(itemDto.getId());
        }
        if (booking.getBooker() != null) {
            UserDto bookerDto = toUserDto(booking.getBooker());
            bookingDtoOut.setBooker(bookerDto);
        }
        return bookingDtoOut;
    }

    public static Booking toBooking(BookingDtoIn bookingDto) {
        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        return booking;
    }

    public static BookingLiteDto toBookingLiteDto(BookingDtoOut bookingDtoOut) {
        if (bookingDtoOut == null) {
            return null;
        }
        BookingLiteDto bookingLiteDto = BookingLiteDto.builder()
                .id(bookingDtoOut.getId())
                .start(bookingDtoOut.getStart())
                .end(bookingDtoOut.getEnd())
                .status(bookingDtoOut.getStatus())
                .build();
        if (bookingDtoOut.getItem() != null) {
            ItemDto item = bookingDtoOut.getItem();
            bookingLiteDto.setItem(item);
        }
        if (bookingDtoOut.getBooker() != null) {
            User booker = toUser(bookingDtoOut.getBooker());
            bookingLiteDto.setBookerId(booker.getId());
        }
        return bookingLiteDto;
    }

    public static BookingItemDto toBookingItemDto(Booking booking) {
        return BookingItemDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}
