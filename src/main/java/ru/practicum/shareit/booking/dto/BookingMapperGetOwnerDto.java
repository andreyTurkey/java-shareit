package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;

public class BookingMapperGetOwnerDto {

    public static BookingGetOwnerDto getBookingGetOwnerDto(Booking booking) {
        return new BookingGetOwnerDto.BookingGetOwnerDtoBuilder()
                .id(booking.getId())
                .bookerId(booking.getUser().getId())
                .itemId(booking.getItem().getId())
                .status(booking.getStatus())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }
}
