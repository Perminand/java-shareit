package ru.practicum.shareit.booking.state;

import java.util.Optional;

public enum BookingState {

    WAITING,
    REJECTED,
    CURRENT,
    FUTURE,
    PAST,
    ALL, ERROR;

    public static Optional<BookingState> from(String state) {
        for (BookingState value : BookingState.values()) {
            if (value.name().equals(state)) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
    }
}