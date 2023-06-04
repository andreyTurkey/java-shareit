package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    Long id;

    @NotBlank(message = "NAME can't be empty.") String name;

    @Email @NotBlank String  email;
}
