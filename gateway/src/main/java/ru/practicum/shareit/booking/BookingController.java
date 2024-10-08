package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    public static final String USER_HEADER = "X-Sharer-User-Id";
    private final BookingClient bookingClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestHeader(USER_HEADER) Long userId, @RequestBody BookingDtoIn bookingDto) {
        log.info("Creating booking {}, userId={}", bookingDto, userId);
        return bookingClient.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> approveBooking(@RequestHeader(USER_HEADER) Long userId,
                                                 @PathVariable("bookingId") long bookingId,
                                                 @RequestParam(name = "approved") String approved) {
        log.info("Patch userId = {}, bookingId = {}, approved = {}", userId, bookingId, approved);
        return bookingClient.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getById(@RequestHeader(USER_HEADER) Long userId,
                                          @PathVariable(name = "bookingId") long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getById(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllForUser(@RequestHeader(USER_HEADER) Long userId,
                                                @RequestParam(name = "state", required = false, defaultValue = "ALL")
                                                String stateParam) {
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        log.info("Get booking with state {}, userId={}", stateParam, userId);
        return bookingClient.getAllForUser(userId, state);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllItemForUser(@RequestHeader(USER_HEADER) Long userId,
                                                    @RequestParam(name = "state", required = false, defaultValue = "ALL")
                                                    String state) {
        log.info("Get userId = {}, state = {}", userId, state);
        return bookingClient.getAllBookingsByOwnerId(userId, state);
    }
}
