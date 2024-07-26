package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.state.BookingState;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
//    @Query(value = "select * from bookings where booker_id = :booker AND status = :status order by start_date desc", nativeQuery = true)
//    List<Booking> findAllByBookerIdAndStatus(Long booker, BookingState status);

    List<Booking> findAllByBookerIdAndStatus(Long booker, BookingState status);

    List<Booking> findAllDistinctBookingByItem_Owner_Id(Long id);

    List<Booking> findAllDistinctBookingByItem_Owner_IdAndStatus(Long id, BookingState status);

    List<Booking> findAllByBookerId(Long userId, Sort sort);

    List<Item> findAllByBookerIdAndItemId(Long bookerId, Long itemId);


//    List<Booking> findAllByIdsIn(List<Long> itemIds);

}
