package ru.practicum.shareit.booking;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.UserDto;

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
