package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class BookingMapper {

    public BookingDto getBookingDto(Booking booking) {
        return new BookingDto.BookingDtoBuilder()
                .id(booking.getId())
                .item(booking.getItem())
                .booker(booking.getUser())
                .status(booking.getStatus())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }

    public Booking getBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setItem(bookingDto.getItem());
        booking.setUser(bookingDto.getBooker());
        booking.setStatus(bookingDto.getStatus());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());

        return booking;
    }
}
