package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    UserRepository mockUserRepository;

    User user = null;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("name")
                .email("email@mail.ru")
                .build();
    }

    @Test
    void getAllUsers() {
        User user2 = User.builder()
                .id(2L)
                .name("name2")
                .email("email2@mail.ru")
                .build();
        Mockito
                .when(mockUserRepository.findAll())
                .thenReturn(List.of(user, user2));

        Assertions.assertEquals(2, userService.getAllUsers().size());

    }

    @Test
    void create() {
        Mockito
                .when(mockUserRepository.save(Mockito.any(User.class)))
                .thenReturn(user);

        Assertions.assertEquals(UserMapper.toUserDto(user), userService.create(UserMapper.toUserDto(user)));
    }

    @Test
    void getById() {
        Mockito
                .when(mockUserRepository.findById(1L))
                .thenReturn(Optional.of(user));
        Assertions.assertEquals(user.getId(), userService.getById(1L).getId());
    }

}