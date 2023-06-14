package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ItemDto {

    Long id;

    @NotBlank(message = "NAME can't be empty.")
    String name;

    @NotNull(message = "DESCRIPTION can't be empty.")
    String description;

    Long owner;

    @NotNull
    Boolean available;
}

