package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingState;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class BookingAddDto {
    Long id;

    Long itemId;

    Long userId;

    BookingState status;

    LocalDateTime start;

    LocalDateTime end;
}
