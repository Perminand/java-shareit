package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.mappers.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {
    @Mock
    RequestRepository requestRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;


    ItemRequest itemRequest = null;
    User user = null;

    @BeforeEach
    void setUp() {
        itemRequest = new ItemRequest(1L, "description",
                new User(1L, "email1@email.ru", "name"),
                LocalDateTime.now(),
                List.of(new Item(),
                        new Item()
                )
        );
        user = User.builder()
                .id(1L)
                .name("name")
                .email("email@mail.ru")
                .build();
    }

    @Test
    void create() {
        Mockito
                .when(requestRepository.save(Mockito.any(ItemRequest.class)))
                .thenReturn(itemRequest);
        Mockito
                .when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));


        Assertions.assertEquals(ItemRequestMapper.toItemRequestDto(itemRequest),
                itemRequestService.create(1L, ItemRequestMapper.toItemRequestDto(itemRequest)));

    }

    @Test
    void findByUserId() {
    }

    @Test
    void findItemRequestOwnerUser() {
    }

    @Test
    void getItemRequest() {
    }
}