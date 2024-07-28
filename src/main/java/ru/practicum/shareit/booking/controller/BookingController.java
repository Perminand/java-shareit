package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody BookingDtoIn bookingDto) {
        log.info("Пришел Post запрос на метод create");
        return bookingService.create(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable("bookingId") long bookingId,
                                     @RequestParam(name = "approved") String approved) {
        log.info("Пришел Patch запрос на метод approveBooking");
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable(name = "bookingId") long bookingId) {
        log.info("Пришел Get запрос на метод getById");
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getAllForUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestParam(name = "state", required = false, defaultValue = "ALL")
                                          String state) {
        log.info("Пришел Get запрос на метод getAllForUser");
        return bookingService.getAllForUser(userId, state);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingDto> getAllItemForUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam(name = "state", required = false, defaultValue = "ALL")
                                              String state) {
        log.info("Пришел Get запрос на метод getAllItemForUser");
        return bookingService.getAllBookingsByOwnerId(userId, state);
    }
}
