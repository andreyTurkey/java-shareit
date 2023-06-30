package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingGetOwnerDto;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@ToString
public class ItemGetNotOwnerDto {

    Long id;

    String name;

    String description;

    Long owner;

    Boolean available;

    BookingGetOwnerDto lastBooking;

    BookingGetOwnerDto nextBooking;
}
