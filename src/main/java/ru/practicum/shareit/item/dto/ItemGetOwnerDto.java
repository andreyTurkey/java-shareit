package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingGetOwnerDto;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@ToString
public class ItemGetOwnerDto {

    Long id;

    String name;

    String description;

    Long owner;

    Boolean available;

    BookingGetOwnerDto lastBooking;

    BookingGetOwnerDto nextBooking;

    List<Long> comments;
}
