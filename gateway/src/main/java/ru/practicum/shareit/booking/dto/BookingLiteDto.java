package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.item.ItemDtoFull;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookingLiteDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDtoFull item;
    private Long bookerId;
    private BookingStatus status;
    private Long itemId;
}