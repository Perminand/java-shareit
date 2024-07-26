package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.state.StateUserBooking;

import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody BookingDto bookingDto) {
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto confirmation(@RequestHeader("X-Sharer-User-Id") Long userId,
                                   @PathVariable("bookingId") long bookingId,
                                   @RequestParam(name = "approved") boolean approved) {
        return bookingService.confirmation(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable(name = "bookingId") long bookingId) {
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getAllForUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestParam(name = "state", required = false, defaultValue = "ALL")
                                          StateUserBooking state) {
        return bookingService.getAllForUser(userId, state);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getAllItemForUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(name = "state", required = false, defaultValue = "ALL")
                                              StateUserBooking state) {
        return bookingService.getAllItemForUser(userId, state);
    }
}
