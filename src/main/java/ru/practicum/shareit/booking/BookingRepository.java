package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.state.BookingStatus;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Item> findAllByBookerIdAndItemId(Long bookerId, Long itemId);

    List<Booking> findAllByBooker_Id(Long bookerId, Sort sort);

    List<Booking> findAllByItem_Owner_Id(Long ownerId);

    @Query("select b from Booking b where b.item.id = :itemId AND b.booker.id = :bookerId AND b.end <= :now")
    List<Booking> findAllByUserIdAndItemIdAndEndDateIsPassed(Long bookerId, Long itemId, LocalDateTime now);

    @Query("select b from Booking b where b.item.id IN :itemsIds")
    List<Booking> findAllByOwnerItems(List<Long> itemsIds, Sort sort);

    @Query("select b from Booking b where b.item.id IN :itemsIds AND b.status = :waiting")
    List<Booking> findAllByOwnerItemsAndWaitingStatus(List<Long> itemsIds, BookingStatus waiting, Sort sort);

    @Query("select b from Booking b where b.item.id IN :itemsIds AND b.status IN :rejected")
    List<Booking> findAllByOwnerItemsAndRejectedStatus(List<Long> itemsIds, List<BookingStatus> rejected, Sort sort);

    @Query("select b from Booking b where b.item.id IN :itemsIds AND b.start < :now AND b.end > :now")
    List<Booking> findAllByOwnerItemsAndCurrentStatus(List<Long> itemsIds, LocalDateTime now, Sort sort);

    @Query("select b from Booking b where b.item.id IN :itemsIds AND b.start > :now")
    List<Booking> findAllByOwnerItemsAndFutureStatus(List<Long> itemsIds, LocalDateTime now, Sort sort);

    @Query("select b from Booking b where b.item.id IN :itemsIds AND b.end < :now")
    List<Booking> findAllByOwnerItemsAndPastStatus(List<Long> itemsIds, LocalDateTime now, Sort sort);

    @Query("select b from Booking b where b.booker.id = :bookerId AND b.status = :waiting")
    List<Booking> findAllByBookerIdAndWaitingStatus(Long bookerId, BookingStatus waiting, Sort sort);

    @Query("select b from Booking b where b.booker.id = :bookerId AND b.status IN :rejected")
    List<Booking> findAllByBookerIdAndRejectedStatus(Long bookerId, List<BookingStatus> rejected, Sort sort);

    @Query("select b from Booking b where b.booker.id = :bookerId AND b.start < :now AND b.end > :now ")
    List<Booking> findAllByBookerIdAndCurrentStatus(Long bookerId, LocalDateTime now, Sort sort);

    @Query("select b from Booking b where b.booker.id = :bookerId AND b.start > :now ")
    List<Booking> findAllByBookerIdAndFutureStatus(Long bookerId, LocalDateTime now, Sort sort);

    @Query("select b from Booking b where b.booker.id = :bookerId AND b.end < :now")
    List<Booking> findAllByBookerIdAndPastStatus(Long bookerId, LocalDateTime now, Sort sort);

    ////    @Query(value = "select * from bookings where booker_id = :booker AND status = :status order by start_date desc", nativeQuery = true)
////    List<Booking> findAllByBookerIdAndStatus(Long booker, BookingStatus status);
//
//    List<Booking> findAllByBookerIdAndStatus(Long booker, BookingStatus status);
//
//    List<Booking> findAllDistinctBookingByItem_Owner_Id(Long id, Sort sort);
//
//    List<Booking> findAllDistinctBookingByItem_Owner_IdAndStartIsAfter(Long id, LocalDateTime start, Sort sort);
//
//    List<Booking> findAllByBookerId(Long userId, Sort sort);
//

//
//    List<Booking> findByBooker_IdAndStartIsAfter(Long bookerId,
//                                                          LocalDateTime start,
//                                                          Sort sort
//    );
//
//    List<Booking> findAllByStartIsAfterAndEndIsBefore(LocalDateTime start, LocalDateTime end);
//
//
////    List<Booking> findAllByIdsIn(List<Long> itemIds);
}
