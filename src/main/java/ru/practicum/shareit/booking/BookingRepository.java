package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingByUserIdEqualsOrderByIdDesc(Long userId);

    List<Booking> findBookingByUserIdEqualsAndStartAfterOrderByIdDesc(Long userId, LocalDateTime now);

    List<Booking> findBookingByUserIdEqualsAndEndBeforeOrderByIdDesc(Long userId, LocalDateTime now);

    List<Booking> findBookingByUserIdEqualsAndStartBeforeAndEndAfter(Long userId, LocalDateTime now,
                                                                     LocalDateTime now1);

    List<Booking> findBookingByUserIdEqualsAndStatusEqualsOrderByIdDesc(Long userId, BookingState status);

    @Query(value = "select *\n" +
            "from bookings\n" +
            "join items i on bookings.item_id = i.id\n" +
            "where owner = ?1\n" +
            "order by id DESC", nativeQuery = true)
    List<Booking> findBookingByOwnerIdOrderByIdDesc(Long ownerId);

    @Query(value = "select *\n" +
            "from bookings\n" +
            "join items i on bookings.item_id = i.id\n" +
            "where owner = ?1\n" +
            "order by id DESC", nativeQuery = true)
    List<Booking> findBookingByOwnerIdAndStartAfterOrderByIdDesc(Long ownerId, LocalDateTime now);

    @Query(value = "select *\n" +
            "from bookings\n" +
            "join items i on bookings.item_id = i.id\n" +
            "where owner = ?1 AND booking_end < now()\n" +
            "order by id DESC", nativeQuery = true)
    List<Booking> findBookingByOwnerIdAndEndBeforeOrderByIdDesc(Long ownerId);

    @Query(value = "select *\n" +
            "from bookings\n" +
            "join items i on bookings.item_id = i.id\n" +
            "where owner = ?1 AND booking_start < now() AND booking_end > now()\n" +
            "order by id DESC", nativeQuery = true)
    List<Booking> findBookingByOwnerIdAndStartBeforeAndEndAfterOrderByIdDesc(Long ownerId);

    @Query(value = "select *\n" +
            "from bookings\n" +
            "join items i on bookings.item_id = i.id\n" +
            "where owner = ?1 AND booking_state LIKE ?2\n" +
            "order by id DESC", nativeQuery = true)
    List<Booking> findBookingByOwnerIdAndStatusEqualsOrderByIdDesc(Long userId, String status);

    @Query(value = "select *\n" +
            "from bookings as b\n" +
            "         join items i on b.item_id = i.id\n" +
            "where owner = ?1 and item_id = ?2 and  booking_start > now()\n" +
            "order by booking_start;", nativeQuery = true)
    List<Booking> findBookingByOwnerIdAndOrderByStart(Long ownerId, Long itemId);

    @Query(value = "select *\n" +
            "from bookings\n" +
            "join items i on bookings.item_id = i.id\n" +
            "where user_id = ?1 AND booking_state NOT LIKE 'REJECTED' AND  booking_end < now()\n", nativeQuery = true)
    List<Booking> findBookingByUserIdAndItemIdAndEndBeforeOrderByIdDesc(Long userId, Long itemId, LocalDateTime now);

    @Query(value = "select *\n" +
            "from bookings as b\n" +
            "         join items i on b.item_id = i.id\n" +
            "where owner = ?1 and item_id = ?2\n" +
            "order by id DESC;", nativeQuery = true)
    List<Booking> findAllBookingByOwnerId(Long userId, Long itemId);

    @Query(value = "select *\n" +
            "from bookings as b\n" +
            "         join items i on b.item_id = i.id\n" +
            "where owner = ?1\n" +
            "order by id DESC;", nativeQuery = true)
    List<Booking> findBookingByUserId(Long userId);
}
