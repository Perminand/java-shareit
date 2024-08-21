package ru.practicum.shareit.booking.repository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.state.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
class BookingRepositoryTest {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private User user1 = null;
    private User user2 = null;

    private Item item1 = null;
    private Item item2 = null;
    private Item item3 = null;
    private Booking bookingWaiting1 = null;
    private Booking bookingWaiting2 = null;
    private Booking bookingWaiting3 = null;
    private static final Sort SORT_DESC = Sort.by(Sort.Direction.DESC, "end");

    @BeforeEach
    void init() {
        itemRepository.deleteAll();
        userRepository.deleteAll();
        bookingRepository.deleteAll();
        this.user1 = new User(null, "email1@mail.ru", "name1");
        this.user2 = new User(null, "email2@mail.ru", "name2");
        userRepository.save(user1);
        userRepository.save(user2);
        this.item1 = new Item(null, "name1", "description1", true, user1, null);
        this.item2 = new Item(null, "name2", "description2", true, user1, null);
        this.item3 = new Item(null, "name3", "description1", true, user2, null);
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        bookingWaiting1 = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(5), item1, user1, BookingStatus.WAITING);
        bookingWaiting2 = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(5), item1, user1, BookingStatus.WAITING);
        bookingWaiting3 = new Booking(null, LocalDateTime.now(), LocalDateTime.now().plusMinutes(5), item1, user2, BookingStatus.WAITING);
    }

    @Test
    void findAllByBookerId() {


        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);
        List<Booking> bookingList = bookingRepository.findAllByBookerId(user1.getId(), Sort.by(Sort.Direction.DESC, "start"));
        assertEquals(2, bookingList.size());
    }

    @Test
    void findAllByItemOwnerId() {


        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);
        List<Booking> bookingList = bookingRepository.findAllByItemOwnerId(user1.getId());
        assertEquals(3, bookingList.size());
    }

    @Test
    void findAllByUserIdAndItemIdAndEndDateIsPassed() {


        bookingWaiting1.setStart(LocalDateTime.now().minusMinutes(10));
        bookingWaiting1.setEnd(LocalDateTime.now().minusMinutes(5));

        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);
        List<Booking> bookingList = bookingRepository.findAllByUserIdAndItemIdAndEndDateIsPassed(user1.getId(), item1.getId(), LocalDateTime.now());
        assertEquals(1, bookingList.size());
    }

    @Test
    void findAllByOwnerItems() {


        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);

        List<Booking> bookingList = bookingRepository.findAllByOwnerItems(List.of(item1.getId()), SORT_DESC);
        assertEquals(3, bookingList.size());
        assertEquals(bookingWaiting3.getId(), bookingList.getFirst().getId());
    }

    @Test
    void findAllByOwnerItemsAndWaitingStatus() {


        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);

        List<Booking> bookingList = bookingRepository.findAllByOwnerItemsAndWaitingStatus(List.of(item1.getId()), BookingStatus.WAITING, SORT_DESC);
        assertEquals(3, bookingList.size());

    }

    @Test
    void findAllByOwnerItemsAndRejectedStatus() {


        Booking bookingRejected1 = bookingWaiting1;
        bookingRejected1.setStatus(BookingStatus.REJECTED);
        Booking bookingRejected2 = bookingWaiting2;
        bookingRejected2.setStatus(BookingStatus.REJECTED);

        bookingRepository.save(bookingRejected1);
        bookingRepository.save(bookingRejected2);
        bookingRepository.save(bookingWaiting3);

        List<Booking> bookingList = bookingRepository.findAllByOwnerItemsAndWaitingStatus(List.of(item1.getId(), item2.getId(), item3.getId()), BookingStatus.REJECTED, SORT_DESC);
        assertEquals(2, bookingList.size());
        assertEquals(bookingRejected2.getId(), bookingList.getFirst().getId());
    }

    @Test
    void findAllByOwnerItemsAndCurrentStatus() {


        bookingWaiting1.setStatus(BookingStatus.APPROVED);
        bookingWaiting2.setStatus(BookingStatus.APPROVED);

        bookingWaiting1.setStart(LocalDateTime.now().minusDays(2));
        bookingWaiting2.setStart(LocalDateTime.now().minusDays(2));


        bookingWaiting1.setEnd(LocalDateTime.now().plusDays(1));
        bookingWaiting2.setEnd(LocalDateTime.now().plusDays(1));

        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);

        List<Booking> bookingList = bookingRepository.findAllByOwnerItemsAndWaitingStatus(List.of(item1.getId(), item2.getId(), item3.getId()), BookingStatus.APPROVED, SORT_DESC);
        assertEquals(2, bookingList.size());
        assertEquals(bookingWaiting2.getId(), bookingList.getFirst().getId());
    }


    @Test
    void findAllByOwnerItemsAndFuture() {


        bookingWaiting1.setStart(LocalDateTime.now().minusDays(2));
        bookingWaiting2.setStart(LocalDateTime.now().minusDays(2));
        bookingWaiting1.setStatus(BookingStatus.APPROVED);
        bookingWaiting2.setStatus(BookingStatus.APPROVED);

        bookingWaiting1.setEnd(LocalDateTime.now().minusDays(1));
        bookingWaiting2.setEnd(LocalDateTime.now().minusDays(1));

        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);

        List<Booking> bookingList = bookingRepository.findAllByOwnerItemsAndWaitingStatus(List.of(item1.getId(), item2.getId(), item3.getId()), BookingStatus.APPROVED, SORT_DESC);
        assertEquals(2, bookingList.size());
        assertEquals(bookingWaiting2.getId(), bookingList.getFirst().getId());
    }

    @Test
    void findAllByOwnerItemsAndPastStatus() {


        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);

        List<Booking> bookingList = bookingRepository.findAllByOwnerItemsAndWaitingStatus(List.of(item1.getId(), item2.getId(), item3.getId()), BookingStatus.WAITING, SORT_DESC);
        assertEquals(3, bookingList.size());
        assertEquals(bookingWaiting3.getId(), bookingList.getFirst().getId());
    }

    @Test
    void findAllByBookerIdAndWaitingStatus() {


        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);

        List<Booking> bookingList = bookingRepository.findAllByOwnerItemsAndWaitingStatus(List.of(item1.getId(), item2.getId(), item3.getId()), BookingStatus.WAITING, SORT_DESC);
        assertEquals(3, bookingList.size());
        assertEquals(bookingWaiting3.getId(), bookingList.getFirst().getId());
    }

    @Test
    void findAllByBookerIdAndRejectedStatus() {
        bookingWaiting1.setStatus(BookingStatus.REJECTED);
        bookingWaiting1.setStatus(BookingStatus.REJECTED);

        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);

        List<Booking> bookingList = bookingRepository.findAllByBookerIdAndRejectedStatus(user1.getId(), List.of(BookingStatus.REJECTED), SORT_DESC);
        assertEquals(1, bookingList.size());
        assertEquals(user2.getId(), bookingList.getFirst().getId());
    }

    @Test
    void findAllByBookerIdAndCurrentStatus() {
        bookingWaiting1.setStatus(BookingStatus.APPROVED);
        bookingWaiting2.setStatus(BookingStatus.APPROVED);

        bookingWaiting1.setStart(LocalDateTime.now().minusDays(2));
        bookingWaiting2.setStart(LocalDateTime.now().minusDays(2));


        bookingWaiting1.setEnd(LocalDateTime.now().plusDays(1));
        bookingWaiting2.setEnd(LocalDateTime.now().plusDays(1));

        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);

        List<Booking> bookingList = bookingRepository.findAllByBookerIdAndCurrentStatus(user1.getId(), LocalDateTime.now(), SORT_DESC);
        assertEquals(2, bookingList.size());
        assertEquals(2L, bookingList.getFirst().getId());
    }

    @Test
    void findAllByBookerIdAndFutureStatus() {
        bookingWaiting1.setStart(LocalDateTime.now().plusDays(2));
        bookingWaiting2.setStart(LocalDateTime.now().plusDays(2));
        bookingWaiting1.setStatus(BookingStatus.APPROVED);
        bookingWaiting2.setStatus(BookingStatus.APPROVED);

        bookingWaiting1.setEnd(LocalDateTime.now().plusDays(4));
        bookingWaiting2.setEnd(LocalDateTime.now().plusDays(4));

        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);

        List<Booking> bookingList = bookingRepository.findAllByBookerIdAndFutureStatus(user1.getId(), LocalDateTime.now(), SORT_DESC);
        assertEquals(2, bookingList.size());
        assertEquals(bookingWaiting2.getId(), bookingList.getFirst().getId());
    }

    @Test
    void findAllByBookerIdAndPastStatus() {
        bookingWaiting1.setStatus(BookingStatus.APPROVED);
        bookingWaiting2.setStatus(BookingStatus.APPROVED);

        bookingWaiting1.setStart(LocalDateTime.now().minusDays(2));
        bookingWaiting2.setStart(LocalDateTime.now().minusDays(2));

        bookingWaiting1.setEnd(LocalDateTime.now().minusDays(1));
        bookingWaiting2.setEnd(LocalDateTime.now().minusDays(1));

        bookingRepository.save(bookingWaiting1);
        bookingRepository.save(bookingWaiting2);
        bookingRepository.save(bookingWaiting3);

        List<Booking> bookingList = bookingRepository.findAllByBookerIdAndPastStatus(user1.getId(), LocalDateTime.now(), SORT_DESC);
        assertEquals(2, bookingList.size());
        assertEquals(bookingWaiting2.getId(), bookingList.getFirst().getId());
    }

}