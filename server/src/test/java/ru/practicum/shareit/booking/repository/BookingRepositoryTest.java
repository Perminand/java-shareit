package ru.practicum.shareit.booking.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.state.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingRepositoryTest {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final RequestRepository requestRepository;

    private final User user1 = new User(null, "email1@mail.ru", "name1");
    private final User user2 = new User(null, "email2@mail.ru", "name2");

    private final Item item1 = new Item(null, "name1", "description1", true, user1, null);
    private final Booking bookingWaiting1 = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(5), item1, user1, BookingStatus.WAITING);
    private final Booking bookingWaiting2 = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(5), item1, user1, BookingStatus.WAITING);
    private final Booking bookingWaiting3 = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(5), item1, user2, BookingStatus.WAITING);
    private final Item item2 = new Item(null, "name2", "description2", true, user1, null);
    private final Item item3 = new Item(null, "name3", "description3", true, user2, null);
    private final Sort SORT_DESC = Sort.by(Sort.Direction.DESC, "end");

    @AfterEach
    public void deleteAll() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        requestRepository.deleteAll();
        bookingRepository.deleteAll();

    }

    @Test
    void findAllByBookerId() {
        userRepository.save(user1);
        userRepository.save(user2);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);
        List<Booking> bookingList = bookingRepository.findAllByBookerId(1L, Sort.by(Sort.Direction.DESC, "start"));
        assertEquals(2, bookingList.size());
    }

    @Test
    void findAllByItemOwnerId() {
        userRepository.save(user1);
        userRepository.save(user2);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);
        List<Booking> bookingList = bookingRepository.findAllByItemOwnerId(1L);
        assertEquals(3, bookingList.size());
    }

    @Test
    void findAllByUserIdAndItemIdAndEndDateIsPassed() {
        userRepository.save(user1);
        userRepository.save(user2);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        bookingWaiting1.setStart(LocalDateTime.now().minusMinutes(10));
        bookingWaiting1.setEnd(LocalDateTime.now().minusMinutes(5));

        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);
        List<Booking> bookingList = bookingRepository.findAllByUserIdAndItemIdAndEndDateIsPassed(1L, 1L, LocalDateTime.now());
        assertEquals(1, bookingList.size());
    }

    @Test
    void findAllByOwnerItems() {
        userRepository.save(user1);
        userRepository.save(user2);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);

        List<Booking> bookingList = bookingRepository.findAllByOwnerItems(List.of(1L), SORT_DESC);
        assertEquals(3, bookingList.size());
        assertEquals(3L, bookingList.getFirst().getId());
    }

    @Test
    void findAllByOwnerItemsAndWaitingStatus() {
        userRepository.save(user1);
        userRepository.save(user2);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);

        List<Booking> bookingList = bookingRepository.findAllByOwnerItemsAndWaitingStatus(List.of(1L), BookingStatus.WAITING, SORT_DESC);
        assertEquals(3, bookingList.size());

    }

    @Test
    void findAllByOwnerItemsAndRejectedStatus() {
        userRepository.save(user1);
        userRepository.save(user2);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        Booking bookingRejected1 = bookingWaiting1;
        bookingRejected1.setStatus(BookingStatus.REJECTED);
        Booking bookingRejected2 = bookingWaiting2;
        bookingRejected2.setStatus(BookingStatus.REJECTED);

        bookingRepository.save(bookingRejected1);
        bookingRepository.save(bookingRejected2);
        bookingRepository.save(bookingWaiting3);

        List<Booking> bookingList = bookingRepository.findAllByOwnerItemsAndWaitingStatus(List.of(1L, 2L, 3L), BookingStatus.REJECTED, SORT_DESC);
        assertEquals(2, bookingList.size());
        assertEquals(2L, bookingList.getFirst().getId());
    }

    @Test
    void findAllByOwnerItemsAndCurrentStatus() {
        userRepository.save(user1);
        userRepository.save(user2);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        bookingWaiting1.setStatus(BookingStatus.APPROVED);
        bookingWaiting2.setStatus(BookingStatus.APPROVED);

        bookingWaiting1.setStart(LocalDateTime.now().minusDays(2));
        bookingWaiting2.setStart(LocalDateTime.now().minusDays(2));


        bookingWaiting1.setEnd(LocalDateTime.now().plusDays(1));
        bookingWaiting2.setEnd(LocalDateTime.now().plusDays(1));

        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);

        List<Booking> bookingList = bookingRepository.findAllByOwnerItemsAndWaitingStatus(List.of(1L, 2L, 3L), BookingStatus.APPROVED, SORT_DESC);
        assertEquals(2, bookingList.size());
        assertEquals(2L, bookingList.getFirst().getId());
    }


    @Test
    void findAllByOwnerItemsAndFuture() {
        userRepository.save(user1);
        userRepository.save(user2);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        bookingWaiting1.setStart(LocalDateTime.now().minusDays(2));
        bookingWaiting2.setStart(LocalDateTime.now().minusDays(2));
        bookingWaiting1.setStatus(BookingStatus.APPROVED);
        bookingWaiting2.setStatus(BookingStatus.APPROVED);

        bookingWaiting1.setEnd(LocalDateTime.now().minusDays(1));
        bookingWaiting2.setEnd(LocalDateTime.now().minusDays(1));

        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);

        List<Booking> bookingList = bookingRepository.findAllByOwnerItemsAndWaitingStatus(List.of(1L, 2L, 3L), BookingStatus.APPROVED, SORT_DESC);
        assertEquals(2, bookingList.size());
        assertEquals(2L, bookingList.getFirst().getId());
    }

    @Test
    void findAllByOwnerItemsAndPastStatus() {
        userRepository.save(user1);
        userRepository.save(user2);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);

        List<Booking> bookingList = bookingRepository.findAllByOwnerItemsAndWaitingStatus(List.of(1L, 2L, 3L), BookingStatus.WAITING, SORT_DESC);
        assertEquals(3, bookingList.size());
        assertEquals(3L, bookingList.getFirst().getId());
    }

    @Test
    void findAllByBookerIdAndWaitingStatus() {
        userRepository.save(user1);
        userRepository.save(user2);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);

        List<Booking> bookingList = bookingRepository.findAllByOwnerItemsAndWaitingStatus(List.of(1L, 2L, 3L), BookingStatus.WAITING, SORT_DESC);
        assertEquals(3, bookingList.size());
        assertEquals(3L, bookingList.getFirst().getId());
    }

    @Test
    void findAllByBookerIdAndRejectedStatus() {
        userRepository.save(user1);
        userRepository.save(user2);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        bookingWaiting1.setStatus(BookingStatus.REJECTED);
        bookingWaiting2.setStatus(BookingStatus.REJECTED);

        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);

        List<Booking> bookingList = bookingRepository.findAllByBookerIdAndRejectedStatus(1L, List.of(BookingStatus.REJECTED), SORT_DESC);
        assertEquals(2, bookingList.size());
        assertEquals(2L, bookingList.getFirst().getId());
    }

    @Test
    void findAllByBookerIdAndCurrentStatus() {
        userRepository.save(user1);
        userRepository.save(user2);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        bookingWaiting1.setStatus(BookingStatus.APPROVED);
        bookingWaiting2.setStatus(BookingStatus.APPROVED);

        bookingWaiting1.setStart(LocalDateTime.now().minusDays(2));
        bookingWaiting2.setStart(LocalDateTime.now().minusDays(2));


        bookingWaiting1.setEnd(LocalDateTime.now().plusDays(1));
        bookingWaiting2.setEnd(LocalDateTime.now().plusDays(1));

        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);

        List<Booking> bookingList = bookingRepository.findAllByBookerIdAndCurrentStatus(1L, LocalDateTime.now(), SORT_DESC);
        assertEquals(2, bookingList.size());
        assertEquals(2L, bookingList.getFirst().getId());
    }

    @Test
    void findAllByBookerIdAndFutureStatus() {
        userRepository.save(user1);
        userRepository.save(user2);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        bookingWaiting1.setStart(LocalDateTime.now().minusDays(2));
        bookingWaiting2.setStart(LocalDateTime.now().minusDays(2));
        bookingWaiting1.setStatus(BookingStatus.APPROVED);
        bookingWaiting2.setStatus(BookingStatus.APPROVED);

        bookingWaiting1.setEnd(LocalDateTime.now().minusDays(1));
        bookingWaiting2.setEnd(LocalDateTime.now().minusDays(1));

        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);

        List<Booking> bookingList = bookingRepository.findAllByBookerIdAndFutureStatus(1L, LocalDateTime.now(), SORT_DESC);
        assertEquals(2, bookingList.size());
        assertEquals(2L, bookingList.getFirst().getId());
    }

    @Test
    void findAllByBookerIdAndPastStatus() {
        userRepository.save(user1);
        userRepository.save(user2);

        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        bookingWaiting1.setStatus(BookingStatus.APPROVED);
        bookingWaiting2.setStatus(BookingStatus.APPROVED);

        bookingWaiting1.setStart(LocalDateTime.now().minusDays(2));
        bookingWaiting2.setStart(LocalDateTime.now().minusDays(2));

        bookingWaiting1.setEnd(LocalDateTime.now().minusDays(1));
        bookingWaiting2.setEnd(LocalDateTime.now().minusDays(1));

        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);

        List<Booking> bookingList = bookingRepository.findAllByBookerIdAndPastStatus(1L, LocalDateTime.now(), SORT_DESC);
        assertEquals(2, bookingList.size());
        assertEquals(2L, bookingList.getFirst().getId());
    }

}