package ru.practicum.shareit.item;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class ItemUpdateDto {

    String name;

    String description;

    Boolean available;
}
