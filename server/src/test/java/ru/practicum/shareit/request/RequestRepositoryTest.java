package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestRepositoryTest {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    private final User user1 = new User(null, "email1@mail.ru", "name1");
    private final User user2 = new User(null, "email2@mail.ru", "name2");

    private final Item item1 = new Item(null, "name1", "description1", true, user1, null);
    private final Item item2 = new Item(null, "name2", "description2", true, user1, null);
    private final Item item3 = new Item(null, "name3", "description3", true, user2, null);

    private final ItemRequest request1 = new ItemRequest(null, "description1", user1, LocalDateTime.now(), null);
    private final ItemRequest request3 = new ItemRequest(null, "description3", user1, LocalDateTime.now().plusMinutes(5), null);
    private final ItemRequest request2 = new ItemRequest(null, "description2", user2, LocalDateTime.now(), null);


    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void findByRequesterId() {
        userRepository.save(user1);
        userRepository.save(user2);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        requestRepository.save(request1);
        requestRepository.save(request2);
        requestRepository.save(request3);
        List<ItemRequest> requestList = requestRepository.findByRequesterId(1L, Sort.by(Sort.Direction.DESC, "created"));
        assertEquals(2, requestList.size());
        assertEquals("description3", requestList.getFirst().getDescription());


    }

    @Test
    void findByRequester_IdNot() {
        userRepository.save(user1);
        userRepository.save(user2);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        requestRepository.save(request1);
        requestRepository.save(request2);
        requestRepository.save(request3);

        assertEquals(1, requestRepository.findByRequester_IdNot(1L, Pageable.ofSize(10)).size());


    }
}