package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mappers.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.dto.BookingDtoOut;
import ru.practicum.shareit.booking.state.BookingState;
import ru.practicum.shareit.exception.error.EntityNotFoundException;
import ru.practicum.shareit.exception.error.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public BookingDtoOut create(Long userId, BookingDtoIn bookingDto) {
        log.info(" ==> /bookings ");
        User user = userCheck(userId);
        Item item = itemCheck(bookingDto.getItemId());
        validate(item, bookingDto);
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingState.WAITING);
        bookingRepository.save(booking);
        return BookingMapper.toBookingDtoOut(booking);
    }

    @Override
    @Transactional
    public BookingDtoOut confirmation(Long userId, long bookingId, boolean approved) {
        userCheck(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Нет booking с заданным id: " + bookingId));
        if (booking.getItem().getOwner().getId().equals(userId)) {
            if (approved) {
                booking.setStatus(BookingState.APPROVED);
            } else {
                booking.setStatus(BookingState.REJECTED);
            }
        }
        bookingRepository.save(booking);
        return BookingMapper.toBookingDtoOut(booking);
    }

    @Override
    public BookingDtoOut getById(Long userId, long bookingId) {
        userCheck(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Нет booking с заданным id: " + bookingId));
        if (booking.getBooker().getId().equals(userId) || booking.getItem().getOwner().getId().equals(userId)) {
            return BookingMapper.toBookingDtoOut(booking);
        } else return null;
    }

    @Override
    public List<BookingDtoOut> getAllForUser(Long userId, String state) {
        BookingState from = BookingState.from(state);
        if (from == null) {
            throw new ValidationException("Unknow state: " + state);
        }
        userCheck(userId);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> bookingList;
        switch (from) {
            case ALL:
                bookingList = bookingRepository.findAllByBookerId(userId, sort);
                break;
            default:
                bookingList = bookingRepository.findAllByBookerIdAndStatus(userId, from);
        }
        List<BookingDtoOut> bookingDtos = bookingList
                .stream()
                .map(BookingMapper::toBookingDtoOut)
                .toList();
        return bookingDtos;
    }

    @Override
    public List<BookingDtoOut> getAllItemForUser(Long userId, BookingState state) {
        userCheck(userId);
        List<Booking> bookingList;
        switch (state) {
            case ALL:
                bookingList = bookingRepository.findAllDistinctBookingByItem_Owner_Id(userId);
                break;
            default:
                bookingList = bookingRepository.findAllDistinctBookingByItem_Owner_IdAndStatus(userId, state);
        }
        return List.of();
    }

    private User userCheck(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Нет user с заданным id: " + userId));

    }

    private Item itemCheck(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Нет item с заданным id: " + itemId));

    }

    private void validate(Item item, BookingDtoIn bookingDto) {
        if (!item.getAvailable()) {
            throw new ValidationException("item available = false");
        }
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new ValidationException("start или end не должно быть null");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            LocalDateTime localDateTime = LocalDateTime.now();
            throw new ValidationException("start не должно быть в прошлом");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) ||
                bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new ValidationException("end раньше start");
        }
    }


//    public Booking create(Long userId, Booking booking) {
//
////        booking.setBooker();
////        return bookingRepository.save()
//    }
}
