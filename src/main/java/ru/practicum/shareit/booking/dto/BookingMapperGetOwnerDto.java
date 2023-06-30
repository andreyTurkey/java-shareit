package ru.practicum.shareit.booking.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;

@Component
public class BookingMapperGetOwnerDto {

    public BookingGetOwnerDto getBookingGetOwnerDto(Booking booking) {
        return new BookingGetOwnerDto.BookingGetOwnerDtoBuilder()
                .id(booking.getId())
                .bookerId(booking.getUser().getId())
                .status(booking.getStatus())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }
}
