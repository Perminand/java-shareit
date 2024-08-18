package ru.practicum.shareit.booking.mappers;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.dto.BookingLiteDto;
import ru.practicum.shareit.item.model.dto.item.ItemDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;

import static ru.practicum.shareit.item.mappers.ItemMapper.toItemDto;
import static ru.practicum.shareit.user.mappers.UserMapper.toUser;
import static ru.practicum.shareit.user.mappers.UserMapper.toUserDto;

public class BookingMapper {

    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .build();
        if (booking.getItem() != null) {
            ItemDto itemDto = toItemDto(booking.getItem());
            bookingDto.setItem(itemDto);
            bookingDto.setItemId(itemDto.getId());
        }
        if (booking.getBooker() != null) {
            UserDto bookerDto = toUserDto(booking.getBooker());
            bookingDto.setBooker(bookerDto);
        }
        return bookingDto;
    }

    public static Booking toBooking(BookingDtoIn bookingDto) {
        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        return booking;
    }

    public static BookingLiteDto toBookingLiteDto(BookingDto bookingDto) {
        if (bookingDto == null) {
            return null;
        }
        BookingLiteDto bookingLiteDto = BookingLiteDto.builder()
                .id(bookingDto.getId())
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(bookingDto.getStatus())
                .build();
        if (bookingDto.getItem() != null) {
            ItemDto item = bookingDto.getItem();
            bookingLiteDto.setItem(item);
        }
        if (bookingDto.getBooker() != null) {
            User booker = toUser(bookingDto.getBooker());
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
