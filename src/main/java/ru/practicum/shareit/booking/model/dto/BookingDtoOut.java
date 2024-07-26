package ru.practicum.shareit.booking.model.dto;

import lombok.Data;
import lombok.ToString;
import ru.practicum.shareit.booking.state.BookingState;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Data
@ToString
public class BookingDtoOut {
    private Long id;
    private Item item;
    //    private LocalDateTime start;
//    private LocalDateTime end;
    private User booker;
    private BookingState status;

}
