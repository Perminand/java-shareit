package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto create(UserDto user);

    UserDto getById(long userId);

    void deleteById(long userId);

    UserDto update(UserDto userDto);
}
