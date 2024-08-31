package ru.practicum.shareit.user.mappers;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail());
    }

    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId() != null ? userDto.getId() : 0)
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static User toUserUpdate(UserDto userDto, User user) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName() != null ? userDto.getName() : user.getName())
                .email(userDto.getEmail() != null ? userDto.getEmail() : user.getEmail())
                .build();
    }
}
