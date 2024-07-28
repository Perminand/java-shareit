package ru.practicum.shareit.booking.mappers;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.dto.BookingDtoOutSmall;
import ru.practicum.shareit.item.model.dto.item.ItemDto;
import ru.practicum.shareit.user.model.dto.UserDto;

import static ru.practicum.shareit.item.mappers.ItemMapper.toItemDto;
import static ru.practicum.shareit.user.mappers.UserMapper.toUserDto;

public class BookingMapper {
    //    public static BookingDtoIn toBookingDto(Booking booking) {
//        BookingDtoIn bookingDto = new BookingDtoIn();
////        bookingDto.setItemId(booking.getItem().getId());
//        bookingDto.setStart(booking.getStart());
//        bookingDto.setEnd(booking.getEnd());
//        bookingDto.setId(booking.getId());
//        return bookingDto;
//    }
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

    public static BookingDtoOutSmall toBookingDtoOutSmall(Booking booking) {
        BookingDtoOutSmall bookingDto = new BookingDtoOutSmall();
        bookingDto.setId(booking.getId());
        bookingDto.setItem(booking.getItem());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setBooker(booking.getBooker());
        return bookingDto;
    }

    public static Booking toBooking(BookingDtoIn bookingDto) {
        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        return booking;
    }
}
