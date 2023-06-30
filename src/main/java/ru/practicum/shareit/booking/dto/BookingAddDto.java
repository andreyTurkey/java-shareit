package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingState;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@ToString
public class BookingAddDto {

    Long id;

    Long itemId;

    Long userId;

    BookingState status;

    @NotNull
    LocalDateTime start;

    @NotNull
    LocalDateTime end;
}
