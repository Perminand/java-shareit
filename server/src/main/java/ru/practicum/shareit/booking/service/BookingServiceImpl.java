package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mappers.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDto;
import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.state.BookingState;
import ru.practicum.shareit.booking.state.BookingStatus;
import ru.practicum.shareit.exception.error.EntityNotFoundException;
import ru.practicum.shareit.exception.error.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private static final Sort SORT_DESC_START = Sort.by(Sort.Direction.DESC, "start");

    @Override
    @Transactional
    public BookingDto create(Long userId, BookingDtoIn bookingDto) {
        User user = userGet(userId);
        Item item = itemGet(bookingDto.getItemId());
        validate(userId, item, bookingDto);
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);
        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto approveBooking(Long ownerId, long bookingId, String approved) {
        userGet(ownerId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ValidationException("Нет booking с заданным id: " + bookingId));

        if (!Objects.equals(booking.getItem().getOwner().getId(), ownerId)) {
            throw new EntityNotFoundException("User with id = " + ownerId + " is not an owner!");
        }

        switch (approved) {
            case "true": {
                if (booking.getStatus().equals(BookingStatus.APPROVED)) {
                    throw new ValidationException("Status is Approved");
                }
                booking.setStatus(BookingStatus.APPROVED);
                break;
            }
            case "false": {
                booking.setStatus(BookingStatus.REJECTED);
                break;
            }
            default:
                throw new ValidationException("Incorrect data in approve method");
        }
        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getById(Long userId, long bookingId) {
        userGet(userId);
        Booking booking = bookingGet(bookingId);
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId) && !Objects.equals(booking.getBooker().getId(), userId)) {
            throw new EntityNotFoundException("User with id = " + userId + " is not an owner!");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getAllForUser(Long userId, BookingState state) {
        userGet(userId);
        List<Booking> bookingList = switch (state) {
            case WAITING ->
                    bookingRepository.findAllByBookerIdAndWaitingStatus(userId, BookingStatus.WAITING, SORT_DESC_START);
            case REJECTED ->
                    bookingRepository.findAllByBookerIdAndRejectedStatus(userId, List.of(BookingStatus.REJECTED, BookingStatus.CANCELED), SORT_DESC_START);
            case CURRENT ->
                    bookingRepository.findAllByBookerIdAndCurrentStatus(userId, LocalDateTime.now(), SORT_DESC_START);
            case FUTURE ->
                    bookingRepository.findAllByBookerIdAndFutureStatus(userId, LocalDateTime.now(), SORT_DESC_START);
            case PAST -> bookingRepository.findAllByBookerIdAndPastStatus(userId, LocalDateTime.now(), SORT_DESC_START);
            case ALL -> bookingRepository.findAllByBookerId(userId, SORT_DESC_START);
        };
        return bookingList
                .stream()
                .map(BookingMapper::toBookingDto)
                .toList();
    }

    @Override
    public List<BookingDto> getAllBookingsByOwnerId(Long ownerId, String state) {
        BookingState from = BookingState.from(state).get();
        userGet(ownerId);
        List<Long> userItemsIds = itemRepository.findByOwnerId(ownerId).stream()
                .map(Item::getId)
                .toList();
        if (userItemsIds.isEmpty()) {
            throw new ValidationException("This method only for users who have >1 items");
        }
        List<Booking> bookingList = switch (from) {
            case WAITING ->
                    bookingRepository.findAllByOwnerItemsAndWaitingStatus(userItemsIds, BookingStatus.WAITING, SORT_DESC_START);
            case REJECTED ->
                    bookingRepository.findAllByOwnerItemsAndRejectedStatus(userItemsIds, List.of(BookingStatus.REJECTED, BookingStatus.CANCELED), SORT_DESC_START);
            case CURRENT ->
                    bookingRepository.findAllByOwnerItemsAndCurrentStatus(userItemsIds, LocalDateTime.now(), SORT_DESC_START);
            case FUTURE ->
                    bookingRepository.findAllByOwnerItemsAndFuture(userItemsIds, LocalDateTime.now(), SORT_DESC_START);
            case PAST ->
                    bookingRepository.findAllByOwnerItemsAndPastStatus(userItemsIds, LocalDateTime.now(), SORT_DESC_START);
            case ALL -> bookingRepository.findAllByOwnerItems(userItemsIds, SORT_DESC_START);
        };
        return bookingList.stream().map(BookingMapper::toBookingDto).toList();
    }

    private User userGet(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Booking: There is no Users with Id: " + userId));

    }

    private Item itemGet(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Booking: There is no Items with Id: " + itemId));

    }

    private Booking bookingGet(long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking: There is no Booking with Id: " + bookingId));

    }

    private void validate(Long userId, Item item, BookingDtoIn bookingDto) {
        if (!item.getAvailable()) {
            throw new ValidationException("Booking: Item is unavailable");
        }
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new ValidationException("Booking: Dates are null!");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) || bookingDto.getStart().isEqual(bookingDto.getEnd())
                || bookingDto.getEnd().isBefore(LocalDateTime.now()) || bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Booking: Problem in dates");
        }
        if (item.getOwner().getId().equals(userId)) {
            throw new EntityNotFoundException("Booking: Owner can't book his item");
        }
    }
}
