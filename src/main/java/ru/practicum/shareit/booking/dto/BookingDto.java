package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Long id;
    private Item item;
    private LocalDateTime start;
    private LocalDateTime end;

}
