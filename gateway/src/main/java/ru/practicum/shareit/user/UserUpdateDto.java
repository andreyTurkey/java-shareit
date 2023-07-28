package ru.practicum.shareit.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class UserUpdateDto {

    Long id;

    String name;

    String email;

    Boolean available;
}
