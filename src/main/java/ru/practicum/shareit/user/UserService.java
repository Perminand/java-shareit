package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    User create(User user);

    UserDto getById(long userId);

    User update(long userId, User user);

    void deleteById(long userId);
}
