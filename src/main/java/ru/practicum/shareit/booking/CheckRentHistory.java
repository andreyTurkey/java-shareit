package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.AvailableException;
import ru.practicum.shareit.item.dto.CommentAddDto;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class CheckRentHistory {

    BookingRepository bookingRepository;

    @Autowired
    public CheckRentHistory(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public void userTookItem(CommentAddDto commentAddDto) {
        LocalDateTime now = LocalDateTime.now();
        List<Booking> allBookingsByUserId = bookingRepository.findBookingByUserIdAndItemIdAndEndBeforeOrderByIdDesc(
                commentAddDto.getUserId(), commentAddDto.getItemId(), now);
        if (allBookingsByUserId.size() == 0) throw new AvailableException("Пользователь не брал вещь в аренду.");
    }
}
