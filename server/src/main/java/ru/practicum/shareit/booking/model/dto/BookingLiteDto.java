package ru.practicum.shareit.booking.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.state.BookingStatus;
import ru.practicum.shareit.item.model.dto.item.ItemDto;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookingLiteDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDto item;
    private Long bookerId;
    private BookingStatus status;
    private Long itemId;
}