package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestComponent;
import ru.practicum.shareit.exception.error.EntityNotFoundException;
import ru.practicum.shareit.exception.error.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepositoryImpl;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

@TestComponent
@RequiredArgsConstructor
class ItemServiceTest {
    private UserRepositoryImpl userRepository = new UserRepositoryImpl();
    private final UserService userService = new UserServiceImpl(userRepository);
    private final ItemRepositoryImpl itemRepository = new ItemRepositoryImpl();
    private final ItemService itemService = new ItemServiceImpl(itemRepository, userRepository);

    @Test
    void getItemsByUserId() {
        create();
        Assertions.assertEquals(1, itemService.getItemsByUserId(1L).size());
    }

    @Test
    void getById() {
        create();
        Assertions.assertEquals("name", itemService.getById(1L).getName());
    }

    @Test
    void create() {
        User user = new User();
        user.setName("name");
        user.setEmail("ru1@ru.ru");
        userService.create(user);
        ItemDto item = new ItemDto(null,"name", "description", true);
        itemService.create(user.getId(), item);
        Assertions.assertEquals(1, itemService.getItemsByUserId(user.getId()).size());
        assertThrows(EntityNotFoundException.class, () ->
                itemService.create(2L, item));
        item.setAvailable(null);
        assertThrows(ValidationException.class, () ->
                itemService.create(1L, item));
    }

    @Test
    void deleteItem() {
        create();
        itemService.deleteItem(1L,1L);
        Assertions.assertEquals(0, itemService.getItemsByUserId(1L).size());
    }

    @Test
    void update() {
        create();
        itemService.update(1L,
                1L,
                new ItemDto(null, "update", "UpdateDescription", true));
        Assertions.assertEquals("update", itemService.getById(1L).getName());
    }

    @Test
    void search() {

    }
}