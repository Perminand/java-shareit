package ru.practicum.shareit.booking.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDtoIn {
    private Long id;
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;

}
