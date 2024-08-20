package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.mappers.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.state.BookingStatus;
import ru.practicum.shareit.exception.error.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @InjectMocks
    BookingServiceImpl bookingService;

    @Mock
    UserRepository userRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    BookingRepository bookingRepository;

    private final User user = User.builder()
            .id(1L)
            .name("user name")
            .email("email@email.com")
            .build();

    private final User owner = User.builder()
            .id(2L)
            .name("username2")
            .email("email2@email.com")
            .build();

    private final UserDto userDto = UserDto.builder()
            .id(1L)
            .name("username")
            .email("email@email.com")
            .build();

    private final Item item = Item.builder()
            .id(1L)
            .name("item name")
            .description("description")
            .available(true)
            .owner(owner)
            .build();

    private final Booking booking = Booking.builder()
            .id(1L)
            .start(LocalDateTime.now().plusDays(1L))
            .end(LocalDateTime.now().plusDays(2L))
            .status(BookingStatus.APPROVED)
            .item(item)
            .booker(user)
            .build();

    private final Booking bookingWaiting = Booking.builder()
            .id(1L)
            .start(LocalDateTime.now().plusDays(1L))
            .end(LocalDateTime.now().plusDays(2L))
            .status(BookingStatus.WAITING)
            .item(item)
            .booker(user)
            .build();

    private final BookingDtoIn bookingDto = BookingDtoIn.builder()
            .itemId(1L)
            .start(LocalDateTime.now().plusDays(1L))
            .end(LocalDateTime.now().plusDays(2L))
            .build();

    private final BookingDtoIn bookingDtoEndBeforeStart = BookingDtoIn.builder()
            .itemId(1L)
            .start(LocalDateTime.now().plusDays(1L))
            .end(LocalDateTime.now().minusDays(1L))
            .build();

    @Test
    void create() {
        BookingDto expectedBookingDtoOut = BookingMapper.toBookingDto(BookingMapper.toBooking(bookingDto));
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(BookingMapper.toBooking(bookingDto));

        BookingDto actualBookingDtoOut = bookingService.create(userDto.getId(), bookingDto);

        assertEquals(BookingMapper.toBookingDto(new Booking(null,
                LocalDateTime.parse("2024-08-21T16:54:06.871976863"),
                LocalDateTime.parse("2024-08-22T16:54:06.871988775"),
                item, user, BookingStatus.WAITING)).getId(), actualBookingDtoOut.getId());
    }

    @Test
    void createWhenEndIsBeforeStartShouldThrowValidationException() {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        ValidationException validationException = assertThrows(ValidationException.class,
                () -> bookingService.create(userDto.getId(), bookingDtoEndBeforeStart));

        assertEquals(validationException.getMessage(), "Дата окончания не может быть раньше или равна дате начала");
    }

    @Test
    void approveBooking() {
    }

    @Test
    void getById() {
    }

    @Test
    void getAllForUser() {
    }

    @Test
    void getAllBookingsByOwnerId() {
    }
}