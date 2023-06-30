package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemServiceDB;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.UserServiceDB;
import ru.practicum.shareit.user.dto.UserMapper;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class BookingAddMapper {

    UserServiceDB userServiceDB;

    ItemServiceDB itemServiceDB;

    ItemMapper itemMapper;

    UserMapper userMapper;

    @Autowired
    public BookingAddMapper(UserServiceDB userServiceDB, ItemServiceDB itemServiceDB, ItemMapper itemMapper, UserMapper userMapper) {
        this.userServiceDB = userServiceDB;
        this.itemServiceDB = itemServiceDB;
        this.itemMapper = itemMapper;
        this.userMapper = userMapper;
    }

    public BookingDto getBookingDto(BookingAddDto bookingAddDto) {
        return new BookingDto.BookingDtoBuilder()
                .id(bookingAddDto.getId())
                .item(itemMapper.getItem(itemServiceDB.getItemById(bookingAddDto.getItemId())))
                .booker(userMapper.getUser(userServiceDB.getUserById(bookingAddDto.getUserId())))
                .status(bookingAddDto.getStatus())
                .start(bookingAddDto.getStart())
                .end(bookingAddDto.getEnd())
                .build();
    }
}
