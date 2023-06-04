package ru.practicum.shareit.item.model;

import jdk.jfr.BooleanFlag;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * TODO Sprint add-controllers.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {

    Long id;

    @NotBlank(message = "NAME can't be empty.") String name;

    @NotBlank(message = "DESCRIPTION can't be empty.") String description;

    @NotNull @Positive Integer owner;

    Boolean isAvailable;
}
