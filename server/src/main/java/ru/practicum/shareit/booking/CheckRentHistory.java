package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.item.dto.CommentAddDto;

import javax.persistence.EntityManager;
import java.util.List;

@Component
@AllArgsConstructor
public class CheckRentHistory {

    final EntityManager em;

    public List<Booking> getAllBookingByUserIdInPastOrderByIdDesc(Long userId) {
        List<Booking> bookings = em.createQuery(
                        "SELECT b from Booking b join Item i on b.item.id = i.id " +
                                "WHERE b.user.id =:userId AND b.status NOT LIKE 'REJECTED' " +
                                "AND  b.end < now() ORDER BY b.id DESC")
                .setParameter("userId", userId)
                .getResultList();
        return bookings;
    }

    public boolean isUserTookItem(CommentAddDto commentAddDto) {
        List<Booking> allBookingsByUserId = getAllBookingByUserIdInPastOrderByIdDesc(commentAddDto.getUserId());
        if (allBookingsByUserId.size() == 0) {
            throw new NotAvailableException("Пользователь не брал вещь в аренду.");
        }
        return true;
    }
}
