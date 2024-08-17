package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.state.BookingState;

import java.util.List;

public interface BookingService {
    BookingDto create(Long userId, BookingDtoIn bookingDto);

    BookingDto approveBooking(Long userId, long bookingId, String approved);

    BookingDto getById(Long userId, long bookingId);

    List<BookingDto> getAllForUser(Long userId, BookingState state);

    List<BookingDto> getAllBookingsByOwnerId(Long userId, String state);

}
