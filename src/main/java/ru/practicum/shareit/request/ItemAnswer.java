package ru.practicum.shareit.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemAnswer {

    Long id;

    Long itemId;

    Long itemRequestId;
}
