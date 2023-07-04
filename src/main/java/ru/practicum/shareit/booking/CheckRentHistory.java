package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.item.dto.CommentAddDto;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class CheckRentHistory {

    private BookingRepository bookingRepository;

    public void isUserTookItem(CommentAddDto commentAddDto) {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> allBookingsByUserId = bookingRepository.findBookingByUserIdAndItemIdAndEndBeforeOrderByIdDesc(
                commentAddDto.getUserId(), commentAddDto.getItemId(), now);
        if (allBookingsByUserId.size() == 0) {
            throw new NotAvailableException("Пользователь не брал вещь в аренду.");
        }
    }
}
