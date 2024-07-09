package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    List<User> getAll();

    User create (User user);

    Optional<User> getById(Long userId);

    User update(User user);

    void deleteById(long userId);
}
