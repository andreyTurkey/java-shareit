package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingAddDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentAddDto;

import java.util.List;

public interface BookingServiceDB {

    BookingDto addBooking(BookingAddDto bookingAddDto);

    BookingDto updateStatus(Long userId, Long bookingId, String  text);

    BookingDto getBookingById(Long bookingId, Long bookerId, Long ownerId);

    List<BookingDto> getAllBookingsByUserId(Long userId, String state);

    List<BookingDto> getAllBookingsByOwnerId(Long ownerId, String state);
}
