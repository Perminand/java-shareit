package ru.practicum.shareit.booking.state;

public enum BookingState {

    WAITING,
    REJECTED,
    CURRENT,
    FUTURE,
    PAST,
    ALL;

    public static BookingState from(String state) {
        for (BookingState value : BookingState.values()) {
            if (value.name().equals(state)) {
                return value;
            }
        }
        return null;
    }
}