package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.error.EntityNotFoundException;
import ru.practicum.shareit.exception.error.ValidationException;
import ru.practicum.shareit.item.mappers.CommentMapper;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.comment.CommentDtoOut;
import ru.practicum.shareit.item.model.dto.item.ItemDtoLite;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @InjectMocks
    ItemServiceImpl itemService;

    @Mock
    ItemRepository itemRepository;
    @Mock
    UserRepository userRepository;

    @Mock
    BookingRepository bookingRepository;
    @Mock
    CommentRepository commentRepository;
    @Mock
    RequestRepository requestRepository;

    User user = new User(1L, "email@email.ru", "name");
    User user2 = new User(2L, "email2@email.ru", "name2");
    ItemRequest request = new ItemRequest(1L, "description", user, LocalDateTime.now(), List.of());
    Item item = new Item(1L, "name", "description", true, user, request);
    Comment comment = new Comment(LocalDateTime.now(), 1L, "text", item, user);
    List<Booking> bookingList = List.of(new Booking(), new Booking(), new Booking());
    ItemDtoLite itemDtoLite = ItemMapper.toItemDtoLite(item);

    @BeforeEach
    void setUp() {


    }

    @Test
    void getItemsByUserId() {

    }

    @Test
    void create() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(requestRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(request));
        when(itemRepository.save(Mockito.any(Item.class))).thenReturn(item);
        Assertions.assertEquals(ItemMapper.toItemDto(item), itemService.create(user.getId(), ItemMapper.toItemDto(item)));
    }


    @Test
    void createComment() {
        when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByUserIdAndItemIdAndEndDateIsPassed(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(LocalDateTime.class)))
                .thenReturn(bookingList);
        when(commentRepository.save(Mockito.any(Comment.class))).thenReturn(comment);
        CommentDtoOut commentDtoOut = itemService.createComment(user.getId(), item.getId(), CommentMapper.toCommentDto(comment));
        Assertions.assertEquals(comment.getId(), commentDtoOut.getId());
    }

    @Test
    void createComment_whenItemIdIsNotValid_thenThrowObjectNotFoundException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        ValidationException entityNotFoundException = assertThrows(ValidationException.class,
                () -> itemService.createComment(user.getId(), item.getId(), CommentMapper.toCommentDto(comment)));

        assertEquals(entityNotFoundException.getMessage(), "This user has no booking");
    }

    @Test
    void createCommentWhenUserHaveNotAnyBookingsShouldThrowValidationException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.findAllByUserBookings(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        ValidationException userBookingsNotFoundException = assertThrows(ValidationException.class,
                () -> itemService.createComment(user.getId(), CommentMapper.toCommentDto(comment)item.getId(), ));

        assertEquals(userBookingsNotFoundException.getMessage(), "У пользователя с id   " + user.getId() + " должно быть хотя бы одно бронирование предмета с id " + item.getId());
    }
}

@Test
void update() {
    when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
    when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
    Assertions.assertEquals(itemDtoLite, itemService.update(1L, 1L, ItemMapper.toItemDto(item)));
}

@Test
void updateItemWhenUserIsNotItemOwnerShouldThrowException() {
    Item updatedItem = Item.builder()
            .id(1L)
            .name("updated name")
            .description("updated description")
            .available(false)
            .owner(user2)
            .build();

    when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(updatedItem));
    when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
    EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
            () -> itemService.update(user.getId(), ItemMapper.toItemDto(item).getId(), ItemMapper.toItemDto(updatedItem)));

    assertEquals(entityNotFoundException.getMessage(), "Попытка изменения item не владельцем");
}

@Test
void updateItemWhenItemIdIsNotValid() {
    EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
            () -> itemService.update(user.getId(), ItemMapper.toItemDto(item).getId(), ItemMapper.toItemDto(item)));
    assertEquals(entityNotFoundException.getMessage(), "Нет user с заданным id: " + user.getId());
}


@Test
void search() {
}

@Test
void getItemById() {
}
}