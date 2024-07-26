package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.state.StateUserBooking;

import java.util.List;

public interface BookingService {
    BookingDto create(Long userId, BookingDto bookingDto);

    BookingDto confirmation(Long userId, long bookingId, boolean approved);

    BookingDto getById(Long userId, long bookingId);

    List<BookingDto> getAllForUser(Long userId, StateUserBooking state);

    List<BookingDto> getAllItemForUser(Long userId, StateUserBooking state);

}
