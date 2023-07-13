package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserMapper;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingMapper {

    public static BookingDto getBookingDto(Booking booking) {
        /*return new BookingDto.BookingDtoBuilder()
                .id(booking.getId())
                .item(ItemMapper.getItemDto(booking.getItem()))
                .booker(UserMapper.getUserDto(booking.getUser()))
                .status(booking.getStatus())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();*/

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setItem(ItemMapper.getItemDto(booking.getItem()));
        bookingDto.setBooker(UserMapper.getUserDto(booking.getUser()));
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());

        return bookingDto;
    }

    public static Booking getBooking(BookingDto bookingDto) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setItem(ItemMapper.getItem(bookingDto.getItem()));
        booking.setUser(UserMapper.getUser(bookingDto.getBooker()));
        booking.setStatus(bookingDto.getStatus());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        return booking;
    }
}
