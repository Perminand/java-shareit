package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.state.StateUserBooking;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    //@Query(value = "select * from bookings where booker = ?1 sorted start_date desc", nativeQuery = true)
    List<Booking> findAllByBookerIdAndStatus(Long booker, StateUserBooking status, Sort sort);

    List<Booking> findAllDistinctBookingByItem_Owner_Id(Long id);

    List<Booking> findAllDistinctBookingByItem_Owner_IdAndStatus(Long id, StateUserBooking status);

    List<Booking> findAllByBookerId(Long userId, Sort sort);

//    List<Booking> findAllByIdsIn(List<Long> itemIds);

}
