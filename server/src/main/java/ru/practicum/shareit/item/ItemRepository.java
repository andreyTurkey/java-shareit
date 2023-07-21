package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findById(Long itemId);

    List<Item> findAllByOwner(Long userId);

    List<Item> findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(String name, String description);

    List<Item> findAllByRequestIdIn(Collection<Long> requestId);

    List<Item> findAllByRequestId(Long requestId);

    @Query("SELECT b from Booking b join Item i on b.item.id = i.id WHERE i.owner =:ownerId " +
            " AND b.item.id =:itemId " +
            " ORDER BY b.id DESC")
    List<Booking> getAllBookingByOwnerIdAndItemId(@Param("ownerId")Long ownerId, @Param("itemId")Long itemId);

    @Query("SELECT b from Booking b join Item i on b.item.id = i.id WHERE i.owner =:id ORDER BY b.id DESC")
    public List<Booking> getAllBookingByOwnerId(@Param("id") Long ownerId);
}
