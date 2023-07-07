package ru.practicum.shareit.user.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@ToString
public class UserDto {

    Long id;

    @NotBlank(message = "NAME can't be empty.")
    String name;

    @Email @NotBlank
    String email;
}
