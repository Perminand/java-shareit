package ru.practicum.shareit.item.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemInfoDto {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class BookingDto {
        Long id;
        Long bookerId;
    }

//    public static class UserDto {
//        Long id;
//        Long name;
//    }
}


