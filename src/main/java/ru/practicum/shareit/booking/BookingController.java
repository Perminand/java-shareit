package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.dto.BookingDtoOut;
import ru.practicum.shareit.booking.state.BookingState;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDtoOut create(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody BookingDtoIn bookingDto) {
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDtoOut confirmation(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @PathVariable("bookingId") long bookingId,
                                      @RequestParam(name = "approved") boolean approved) {
        return bookingService.confirmation(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDtoOut getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable(name = "bookingId") long bookingId) {
        log.info("Getting info for booking: {}", bookingId);
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDtoOut> getAllForUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestParam(name = "state", required = false, defaultValue = "ALL")
                                          String state) {
        List<BookingDtoOut> bookingDtoOutList = bookingService.getAllForUser(userId, state);
        return bookingDtoOutList;
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDtoOut> getAllItemForUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(name = "state", required = false, defaultValue = "ALL")
                                              BookingState state) {
        return bookingService.getAllItemForUser(userId, state);
    }
}
