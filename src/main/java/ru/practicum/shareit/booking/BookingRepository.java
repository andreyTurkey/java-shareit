package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByItem_Id(Long itemId);

    @Query("SELECT b FROM Booking b WHERE b.user.id = :id")
    Page<Booking> findBookingByUserId(@Param("id") Long userId, Pageable page);

    @Query("SELECT b FROM Booking b JOIN Item  i on b.item.id = i.id WHERE i.owner = :id")
    Page<Booking> findBookingByOwnerId(@Param("id") Long ownerId, Pageable page);

    @Query("select b from Booking b where b.id in :ids ORDER BY b.id DESC")
    List<Booking> findAllByBookingsId(@Param("ids") List<Long> ids);
}
