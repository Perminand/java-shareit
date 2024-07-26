package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.dto.BookingDtoOut;
import ru.practicum.shareit.booking.state.BookingState;

import java.util.List;

public interface BookingService {
    BookingDtoOut create(Long userId, BookingDtoIn bookingDto);

    BookingDtoOut confirmation(Long userId, long bookingId, boolean approved);

    BookingDtoOut getById(Long userId, long bookingId);

    List<BookingDtoOut> getAllForUser(Long userId, String state);

    List<BookingDtoOut> getAllItemForUser(Long userId, BookingState state);

}
