package ru.practicum.shareit.booking.mappers;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.dto.BookingDtoOutSmall;

public class BookingMapper {
    //    public static BookingDtoIn toBookingDto(Booking booking) {
//        BookingDtoIn bookingDto = new BookingDtoIn();
////        bookingDto.setItemId(booking.getItem().getId());
//        bookingDto.setStart(booking.getStart());
//        bookingDto.setEnd(booking.getEnd());
//        bookingDto.setId(booking.getId());
//        return bookingDto;
//    }
    public static BookingDtoOut toBookingDtoOut(Booking booking) {
        BookingDtoOut bookingDto = new BookingDtoOut();
        bookingDto.setId(booking.getId());
        bookingDto.setItem(booking.getItem());
//        bookingDto.setStart(booking.getStart());
//        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setBooker(booking.getBooker());
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
