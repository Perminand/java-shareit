package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.mappers.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.dto.BookingDtoOut;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.state.BookingState;
import ru.practicum.shareit.booking.state.BookingStatus;
import ru.practicum.shareit.exception.error.EntityNotFoundException;
import ru.practicum.shareit.exception.error.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
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
        BookingDtoOut expectedBookingDtoOut = BookingMapper.toBookingDto(BookingMapper.toBooking(bookingDto));
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(BookingMapper.toBooking(bookingDto));

        BookingDtoOut actualBookingDtoOut = bookingService.create(userDto.getId(), bookingDto);

        assertEquals(BookingMapper.toBookingDto(new Booking(null,
                LocalDateTime.parse("2024-08-21T16:54:06.871976863"),
                LocalDateTime.parse("2024-08-22T16:54:06.871988775"),
                item, user, BookingStatus.WAITING)).getId(), actualBookingDtoOut.getId());
    }

//    @Test
//    void createWhenEndIsBeforeStartShouldThrowValidationException() {
//        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
//        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
//
//        ValidationException validationException = assertThrows(ValidationException.class,
//                () -> bookingService.create(userDto.getId(), bookingDtoEndBeforeStart));
//
//        assertEquals(validationException.getMessage(), "Дата окончания не может быть раньше или равна дате начала");
//    }

    @Test
    void createWhenItemIsNotAvailableShouldThrowValidationException() {
        item.setAvailable(false);
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        ValidationException bookingValidationException = assertThrows(ValidationException.class,
                () -> bookingService.create(userDto.getId(), bookingDto));

        assertEquals(bookingValidationException.getMessage(), "Booking: Item is unavailable");
    }

    @Test
    void createWhenItemOwnerEqualsBookerShouldThrowValidationException() {
        item.setOwner(user);
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        ValidationException entityNotFoundException = assertThrows(ValidationException.class,
                () -> bookingService.create(userDto.getId(), bookingDto));

        assertEquals(entityNotFoundException.getMessage(), "Booking: Owner can't book his item");
    }

    @Test
    void update() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingWaiting));
        when(bookingRepository.save(any(Booking.class))).thenReturn(bookingWaiting);

        BookingDtoOut actualBookingDtoOut = bookingService.approveBooking(owner.getId(), bookingWaiting.getId(), true);

        assertEquals(BookingStatus.APPROVED, actualBookingDtoOut.getStatus());
    }

    @Test
    void updateWhenStatusNotApproved() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(bookingWaiting));
        when(bookingRepository.save(any(Booking.class))).thenReturn(bookingWaiting);

        BookingDtoOut actualBookingDtoOut = bookingService.approveBooking(owner.getId(), bookingWaiting.getId(), false);

        assertEquals(BookingStatus.REJECTED, actualBookingDtoOut.getStatus());
    }

    @Test
    void updateShouldStatusNotWaiting() {
        booking.setStatus(BookingStatus.REJECTED);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        ValidationException bookingValidationException = assertThrows(ValidationException.class,
                () -> bookingService.approveBooking(owner.getId(), booking.getId(), false));

        assertEquals(bookingValidationException.getMessage(), "Бронь не cо статусом WAITING");
    }

    @Test
    void updateWhenUserIsNotItemOwnerShouldThrowNotFoundException() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        ValidationException entityNotFoundException = assertThrows(ValidationException.class,
                () -> bookingService.approveBooking(userDto.getId(), booking.getId(), true));

        assertEquals(entityNotFoundException.getMessage(), "User with id = 1 is not an owner!");
    }

    @Test
    void getById() {
        BookingDtoOut expectedBookingDtoOut = BookingMapper.toBookingDto(booking);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingDtoOut actualBookingDtoOut = bookingService.getById(user.getId(), booking.getId());

        assertEquals(expectedBookingDtoOut, actualBookingDtoOut);
    }

    @Test
    void getByIdWhenBookingIdIsNotValidShouldThrowObjectNotFoundException() {

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> bookingService.getById(1L, booking.getId()));

        assertEquals(entityNotFoundException.getMessage(), "Booking: There is no Users with Id: 1");
    }

    @Test
    void getByIdWhenUserIsNotItemOwnerShouldThrowObjectNotFoundException() {

        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> bookingService.getById(3L, booking.getId()));

        assertEquals(entityNotFoundException.getMessage(), "Booking: There is no Users with Id: 3");
    }

    @Test
    void getAllByBookerWhenBookingStateAll() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toBookingDto(booking));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerId(anyLong(), any(Sort.class))).thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.getAllForUser(user.getId(), BookingState.ALL);

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void getAllByBooker_whenBookingStateCURRENT() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toBookingDto(booking));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndCurrentStatus(anyLong(), any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.getAllForUser(user.getId(), BookingState.CURRENT);

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void getAllByBookerWhenBookingStatePAST() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toBookingDto(booking));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndPastStatus(anyLong(), any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.getAllForUser(user.getId(), BookingState.PAST);

        assertEquals(expectedBookingsDtoOut.size(), actualBookingsDtoOut.size());
    }

    @Test
    void getAllByBookerWhenBookingStateFUTURE() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toBookingDto(booking));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndFutureStatus(anyLong(), any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.getAllForUser(user.getId(), BookingState.FUTURE);

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void getAllByBookerWhenBookingStateWAITING() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toBookingDto(booking));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndWaitingStatus(anyLong(), any(BookingStatus.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.getAllForUser(user.getId(), BookingState.WAITING);

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void getAllByBookerWhenBookingStateIsNotValidShouldThrowIllegalArgumentException() {
        assertThrows(EntityNotFoundException.class,
                () -> bookingService.getAllForUser(user.getId(), BookingState.ERROR));
    }

    @Test
    void getAllByOwnerWhenBookingStateAll() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toBookingDto(booking));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByOwnerItems(any(List.class), any(Sort.class))).thenReturn(List.of(booking));
        when(itemRepository.findByOwnerId(anyLong())).thenReturn(List.of(item));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.getAllBookingsByOwnerId(user.getId(), "ALL");

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void getAllByOwnerWhenBookingStateCURRENT() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toBookingDto(booking));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findByOwnerId(anyLong())).thenReturn(List.of(item));
        when(bookingRepository.findAllByOwnerItemsAndCurrentStatus(any(List.class), any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.getAllBookingsByOwnerId(user.getId(), "CURRENT");

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void getAllByOwnerWhenBookingStatePAST() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toBookingDto(booking));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findByOwnerId(anyLong())).thenReturn(List.of(item));
        when(bookingRepository.findAllByOwnerItemsAndPastStatus(any(List.class), any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.getAllBookingsByOwnerId(user.getId(), "PAST");

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void getAllByOwnerWhenBookingStateFUTURE() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toBookingDto(booking));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findByOwnerId(anyLong())).thenReturn(List.of(item));
        when(bookingRepository.findAllByOwnerItemsAndFuture(any(List.class), any(LocalDateTime.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.getAllBookingsByOwnerId(user.getId(), "FUTURE");

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void getAllByOwnerWhenBookingStateWAITING() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toBookingDto(booking));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findByOwnerId(anyLong())).thenReturn(List.of(item));
        when(bookingRepository.findAllByOwnerItemsAndWaitingStatus(any(List.class), any(BookingStatus.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.getAllBookingsByOwnerId(user.getId(), "WAITING");

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void getAllByOwnerWhenBookingStateREJECTED() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toBookingDto(booking));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findByOwnerId(anyLong())).thenReturn(List.of(item));
        when(bookingRepository.findAllByOwnerItemsAndRejectedStatus(any(List.class), any(List.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.getAllBookingsByOwnerId(user.getId(), "REJECTED");

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void getAllByBookerWhenBookingStateREJECTED() {
        List<BookingDtoOut> expectedBookingsDtoOut = List.of(BookingMapper.toBookingDto(booking));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingRepository.findAllByBookerIdAndRejectedStatus(anyLong(), any(List.class), any(Sort.class)))
                .thenReturn(List.of(booking));

        List<BookingDtoOut> actualBookingsDtoOut = bookingService.getAllForUser(user.getId(), BookingState.REJECTED);

        assertEquals(expectedBookingsDtoOut, actualBookingsDtoOut);
    }

    @Test
    void getAllByOwnerWhenBookingStateIsNotValidThenThrowIllegalArgumentException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        assertThrows(ValidationException.class,
                () -> bookingService.getAllBookingsByOwnerId(user.getId(), "ERROR"));
    }
}