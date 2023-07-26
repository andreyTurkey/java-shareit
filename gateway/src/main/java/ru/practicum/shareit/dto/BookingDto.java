package ru.practicum.shareit.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingState;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class BookingDto {

    Long id;

    ItemDto item;

    UserDto booker;

    BookingState status;

    @NotNull
    LocalDateTime start;

    @NotNull
    LocalDateTime end;
}
