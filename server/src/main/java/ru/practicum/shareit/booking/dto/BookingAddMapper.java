package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingAddMapper {

    public static BookingDto getBookingDto(BookingAddDto bookingAddDto, ItemDto item, User booker) {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(bookingAddDto.getId());
        bookingDto.setItem(item);
        bookingDto.setBooker(UserMapper.getUserDto(booker));
        bookingDto.setStatus(bookingAddDto.getStatus());
        bookingDto.setStart(bookingAddDto.getStart());
        bookingDto.setEnd(bookingAddDto.getEnd());

        return bookingDto;
    }
}
