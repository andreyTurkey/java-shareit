package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByItem_Id(Long itemId);

    Page<Booking> findBookingByUserId(Long userId, Pageable page);

    @Query("SELECT b FROM Booking b JOIN Item  i on b.item.id = i.id WHERE i.owner = :id")
    Page<Booking> findBookingByOwnerId(@Param("id") Long ownerId, Pageable page);

    List<Booking> findAllByIdInOrderByIdDesc(Collection<Long> ids);

    @Query("SELECT b from Booking b WHERE b.user.id =:id AND b.start > now() ORDER BY id DESC")
    List<Booking> getAllByUserIdAndStartIsAfterNow(@Param("id") Long userId);

    @Query("SELECT b from Booking b WHERE b.user.id =:id AND b.end < now() ORDER BY id DESC")
    List<Booking> getAllBookingsByUserIdPast(@Param("id") Long userId);

    @Query("SELECT b from Booking b WHERE b.user.id =:id AND b.start <= now() AND b.end >= now()")
    List<Booking> getAllBookingsByUserIdCurrent(@Param("id") Long userId);

    @Query("SELECT b from Booking b WHERE b.user.id =:id AND b.status  like :state")
    List<Booking> getAllBookingsByUserIdAndStatus(@Param("id") Long userId, @Param("state") BookingState state);

    @Query("SELECT b from Booking b join Item i on b.item.id = i.id WHERE i.owner =:id ORDER BY b.id DESC")
    List<Booking> getAllBookingByOwnerId(@Param("id") Long ownerId);

    @Query("SELECT b from Booking b join Item i on b.item.id = i.id " +
            "WHERE i.owner =:id AND b.start >= now() ORDER BY b.id DESC")
    List<Booking> getAllBookingByOwnerIdFuture(@Param("id") Long ownerId);

    @Query("SELECT b from Booking b join Item i on b.item.id = i.id WHERE i.owner =:id " +
            " AND b.end <= now() ORDER BY b.id DESC")
    List<Booking> getAllBookingByOwnerIdPast(@Param("id") Long ownerId);

    @Query("SELECT b from Booking b join Item i on b.item.id = i.id WHERE i.owner =:id " +
            " AND b.start <= now() AND  b.end >= now() ORDER BY b.id DESC")
    List<Booking> getAllBookingByOwnerIdCurrent(@Param("id") Long ownerId);

    @Query("SELECT b from Booking b join Item i on b.item.id = i.id WHERE i.owner =:id " +
            " AND b.status  like :state ORDER BY b.id DESC")
    List<Booking> getAllBookingByOwnerIdStatus(@Param("id") Long ownerId, @Param("state") BookingState state);
}
