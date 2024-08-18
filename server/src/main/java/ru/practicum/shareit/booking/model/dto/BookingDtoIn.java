package ru.practicum.shareit.booking.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingDtoIn {
    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;

}
