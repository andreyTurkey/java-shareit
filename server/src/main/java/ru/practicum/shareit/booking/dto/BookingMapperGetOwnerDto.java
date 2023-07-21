package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;

public class BookingMapperGetOwnerDto {

    public static BookingGetOwnerDto getBookingGetOwnerDto(Booking booking) {
        BookingGetOwnerDto bookingGetOwnerDto = new BookingGetOwnerDto();
        bookingGetOwnerDto.setId(booking.getId());
        bookingGetOwnerDto.setBookerId(booking.getUser().getId());
        bookingGetOwnerDto.setItemId(booking.getItem().getId());
        bookingGetOwnerDto.setStatus(booking.getStatus());
        bookingGetOwnerDto.setStart(booking.getStart());
        bookingGetOwnerDto.setEnd(booking.getEnd());

        return bookingGetOwnerDto;
    }
}
