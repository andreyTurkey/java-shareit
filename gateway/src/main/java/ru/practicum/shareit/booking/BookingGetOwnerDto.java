package ru.practicum.shareit.booking;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingState;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class BookingGetOwnerDto {

    Long id;

    Long bookerId;

    Long itemId;

    BookingState status;

    LocalDateTime start;

    LocalDateTime end;
}
