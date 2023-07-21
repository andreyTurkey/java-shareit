package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingGetOwnerDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class ItemPublicDto {

    Long id;

    @NotBlank(message = "NAME can't be empty.")
    String name;

    @NotNull(message = "DESCRIPTION can't be empty.")
    String description;

    Long owner;

    @NotNull
    Boolean available;

    BookingGetOwnerDto lastBooking;

    BookingGetOwnerDto nextBooking;

    List<CommentPublicDto> comments;

    Long requestId;
}
