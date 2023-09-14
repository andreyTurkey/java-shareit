package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingGetOwnerDto;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ItemPublicDto {

    Long id;

    String name;

    String description;

    Long owner;

    Boolean available;

    BookingGetOwnerDto lastBooking;

    BookingGetOwnerDto nextBooking;

    List<CommentPublicDto> comments;

    Long requestId;
}
