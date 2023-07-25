package ru.practicum.shareit.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class UserDto {

    Long id;

    @NotBlank(message = "NAME can't be empty.")
    String name;

    @Email @NotBlank
    String email;
}
