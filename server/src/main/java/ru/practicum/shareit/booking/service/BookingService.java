package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.dto.BookingDtoOut;
import ru.practicum.shareit.booking.state.BookingState;

import java.util.List;

public interface BookingService {
    BookingDtoOut create(Long userId, BookingDtoIn bookingDto);

    BookingDtoOut approveBooking(Long userId, long bookingId, Boolean approved);

    BookingDtoOut getById(Long userId, long bookingId);

    List<BookingDtoOut> getAllForUser(Long userId, BookingState state);

    List<BookingDtoOut> getAllBookingsByOwnerId(Long userId, String state);

}
