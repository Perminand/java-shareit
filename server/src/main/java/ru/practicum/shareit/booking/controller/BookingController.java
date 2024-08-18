package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.state.BookingState;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    public static final String USER_HEADER = "X-Sharer-User-Id";
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestHeader(USER_HEADER) Long userId, @RequestBody BookingDtoIn bookingDto) {
        log.info("Creating booking {}, userId={}", bookingDto, userId);
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto approveBooking(@RequestHeader(USER_HEADER) Long userId,
                                     @PathVariable("bookingId") long bookingId,
                                     @RequestParam(name = "approved") String approved) {
        log.info("Approve userId = {}, bookingId = {}, approved = {}", userId, bookingId, approved);
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getById(@RequestHeader(USER_HEADER) Long userId,
                              @PathVariable(name = "bookingId") long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getAllForUser(@RequestHeader(USER_HEADER) Long userId,
                                          @RequestParam(name = "state", required = false, defaultValue = "ALL")
                                          String stateParam) {
        log.info("Get userId = {},  state = {}", userId, stateParam);
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingService.getAllForUser(userId, state);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getAllItemForUser(@RequestHeader(USER_HEADER) Long userId,
                                              @RequestParam(name = "state", required = false, defaultValue = "ALL")
                                              String state) {
        log.info("Get userId = {}, state = {}", userId, state);
        return bookingService.getAllBookingsByOwnerId(userId, state);
    }
}
