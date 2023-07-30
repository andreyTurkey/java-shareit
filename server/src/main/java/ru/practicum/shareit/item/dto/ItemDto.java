package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ItemDto {

    Long id;

    String name;

    String description;

    Long owner;

    Boolean available;

    Long requestId;
}

