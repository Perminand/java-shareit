package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestComponent;
import ru.practicum.shareit.exception.error.DuplicatedDataException;
import ru.practicum.shareit.exception.error.ValidationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@TestComponent
@RequiredArgsConstructor
class UserServiceTest {
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final UserService userService = new UserServiceImpl(userRepository);


    @Test
    void getAllUsers() {
        create();
    }

    @Test
    void create() {
        User user = new User();
        user.setName("name");
        user.setEmail("ru1@ru.ru");
        userService.create(user);
        Assertions.assertEquals(1L, userService.getAllUsers().size());
        DuplicatedDataException duplicatedDataException = assertThrows(DuplicatedDataException.class, () ->
                userService.create(user));
        user.setEmail(null);
        ValidationException validationException = assertThrows(ValidationException.class, () ->
                userService.create(user));
        user.setEmail("ru.com");
        ValidationException validationException2 = assertThrows(ValidationException.class, () ->
                userService.create(user));
    }

    @Test
    void getById() {
    }

    @Test
    void update() {
        User user = new User();
        user.setName("name");
        user.setEmail("ru1@ru.ru");
        user = userService.create(user);
        user.setName("updateName");
        userService.update(user.getId(), user);
        user.setEmail("ru3@ru.ru");
        userService.update(user.getId(), user);
        User user2 = new User();
        user2.setName("name2");
        user2.setEmail("ru2@ru.ru");
        userService.create(user2);
        User finalUser = user;
        DuplicatedDataException duplicatedDataException = assertThrows(DuplicatedDataException.class, () ->
                userService.update(2L, finalUser));
        user.setEmail(null);
    }

    @Test
    void deleteById() {
        User user = new User();
        user.setName("name2");
        user.setEmail("ru2@ru.ru");
        userService.create(user);
        int count = userService.getAllUsers().size();
        userService.deleteById(1L);
        Assertions.assertEquals(count - 1, userService.getAllUsers().size());
    }
}