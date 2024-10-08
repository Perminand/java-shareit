package ru.practicum.shareit.item.model.dto.item;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.dto.BookingLiteDto;
import ru.practicum.shareit.item.model.dto.comment.CommentDto;

import java.util.List;

@Data
@Builder
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingLiteDto nextBooking;
    private List<CommentDto> comments;
    private BookingLiteDto lastBooking;
    private Long ownerId;
    private Long requestId;
}

