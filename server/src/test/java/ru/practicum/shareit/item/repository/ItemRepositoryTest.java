package ru.practicum.shareit.item.repository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
class ItemRepositoryTest {


    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final User user1 = new User(null, "email1@mail.ru", "name1");
    private final User user2 = new User(null, "email2@mail.ru", "name2");

    private final Item item1 = new Item(null, "name1", "description1", true, user1, null);
    private final Item item2 = new Item(null, "name2", "description2", true, user1, null);
    private final Item item3 = new Item(null, "name3", "description3", true, user2, null);

    @AfterEach
    public void deleteAll() {
        itemRepository.deleteAll();
        userRepository.deleteAll();

    }

    @Test
    void findByOwnerId() {
        userRepository.save(user1);
        userRepository.save(user2);

        itemRepository.save(item1);
        itemRepository.save(item2);

        List<Item> items = itemRepository.findByOwnerId(user1.getId());
        assertEquals(items.size(), 2);
        assertEquals(items.getFirst().getName(), "name1");
    }

    @Test
    void findByOwnerIdSort() {
        userRepository.save(user1);
        userRepository.save(user2);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        List<Item> items = itemRepository.findByOwnerId(user1.getId());
        assertEquals(items.size(), 2);
        assertEquals(items.getFirst().getName(), "name1");
    }

    @Test
    void deleteByOwnerIdAndId() {
        userRepository.save(user1);
        userRepository.save(user2);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        itemRepository.deleteByOwnerIdAndId(user1.getId(), item2.getId());

        assertEquals(1, itemRepository.findByOwnerId(user1.getId()).size());
    }

    @Test
    void findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase() {
        userRepository.save(user1);
        userRepository.save(user2);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        List<Item> itemListForName = itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                "name2",
                "name2"
        );
        List<Item> itemListForDescription = itemRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                "description2",
                "description2"
        );
        assertEquals(1, itemListForName.size());
        assertEquals("name2", itemListForName.getFirst().getName());
        assertEquals(1, itemListForDescription.size());
        assertEquals("description2", itemListForDescription.getFirst().getDescription());

    }
}