package ru.practicum.shareit.item.dto.item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingLiteDto;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.user.markers.Update;

import java.util.List;

@Data
@Builder
public class ItemDtoFull {
    @NotNull(groups = Update.class)
    private Long id;
    @NotNull
    @NotBlank
    @Size(max = 50)
    private String name;
    @NotNull
    @NotBlank
    @Size(max = 250)
    private String description;
    @NotNull
    private Boolean available;
    @Min(0)
    private Long requestId;
    private BookingLiteDto nextBooking;
    private BookingLiteDto lastBooking;
    @Min(0)
    private Long ownerId;
    private List<CommentDto> comments;

}

