package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.TestComponent;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserRepositoryImpl;

@TestComponent
@RequiredArgsConstructor
class ItemRepositoryTest {
private final UserRepository userRepository = new UserRepositoryImpl();
private final ItemRepository itemRepository = new ItemRepositoryImpl();

    @Test
    void getItems() {
        create();
        Assertions.assertEquals(1, itemRepository.getItems(1L).size());
    }

    @Test
    void create() {
        User user = new User();
        user.setName("name");
        user.setEmail("ru1@ru.ru");
        user = userRepository.create(user);
        Item item = new Item(null, "name", "description", true, user, null);
        itemRepository.create(user.getId(), item);
        Assertions.assertEquals("name", itemRepository.getById(1L).get().getName());
    }

    @Test
    void deleteByUserIdAndItemId() {
        create();
        itemRepository.deleteByUserIdAndItemId(1L, 1L);
        Assertions.assertEquals(0, itemRepository.getItems(1L).size());
    }

    @Test
    void getById() {
        create();
        Assertions.assertEquals("name", itemRepository.getById(1L).get().getName());
    }

    @Test
    void update() {
        create();
        User user = userRepository.getById(1L).get();
        Item item = new Item(1L, "update", "description", true, user, null);
        itemRepository.update(item);
        Assertions.assertEquals("update", itemRepository.getById(1L).get().getName());
    }

    @Test
    void search() {
    }

    @Test
    void createRequest() {
    }
}