package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingState;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@ToString
public class BookingGetOwnerDto {

    Long id;

    Long bookerId;

    Long itemId;

    BookingState status;

    LocalDateTime start;

    LocalDateTime end;
}
