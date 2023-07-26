package ru.practicum.shareit.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestPublicDto {

    Long id;

    String description;

    LocalDateTime created;

    List<ItemPublicDto> items;
}
