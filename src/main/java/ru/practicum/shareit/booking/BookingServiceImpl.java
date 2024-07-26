package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mappers.BookingMapper;
import ru.practicum.shareit.booking.state.StateUserBooking;
import ru.practicum.shareit.booking.state.StatusBooking;
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
    public BookingDto create(Long userId, BookingDto bookingDto) {
        log.info(" ==> /bookings ");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Нет user с заданным id: " + userId));
        Item item = itemRepository.findById(bookingDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Нет item с заданным id: " + userId));
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
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(StatusBooking.WAITING);
        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto confirmation(Long userId, long bookingId, boolean approved) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Нет user с заданным id: " + userId));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Нет booking с заданным id: " + bookingId));
        if (booking.getItem().getOwner().getId().equals(userId)) {
            if (approved) {
                booking.setStatus(StatusBooking.APPROED);
            } else {
                booking.setStatus(StatusBooking.CANCELED);
            }
        }
        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getById(Long userId, long bookingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Нет user с заданным id: " + userId));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Нет booking с заданным id: " + bookingId));
        if (booking.getBooker().getId().equals(userId) || booking.getItem().getOwner().getId().equals(userId)) {
            return BookingMapper.toBookingDto(booking);
        } else return null;
    }

    @Override
    public List<BookingDto> getAllForUser(Long userId, StateUserBooking state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Нет user с заданным id: " + userId));
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> bookingList;
        switch (state) {
            case ALL:
                bookingList = bookingRepository.findAllByBookerId(userId, sort);
                break;
            default:
                bookingList = bookingRepository.findAllByBookerIdAndStatus(userId, state, sort);
        }
        List<BookingDto> bookingDtos = bookingList.stream()
                .map(BookingMapper::toBookingDto).toList();
        return bookingDtos;
    }

    @Override
    public List<BookingDto> getAllItemForUser(Long userId, StateUserBooking state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Нет user с заданным id: " + userId));
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


//    public Booking create(Long userId, Booking booking) {
//
////        booking.setBooker();
////        return bookingRepository.save()
//    }
}
