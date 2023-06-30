package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
public class ItemDto {

    Long id;

    @NotBlank(message = "NAME can't be empty.")
    String name;

    @NotNull(message = "DESCRIPTION can't be empty.")
    String description;

    Long owner;

    @NotNull
    Boolean available;

    @Override
    public String toString() {
        return "ItemDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", owner=" + owner +
                ", available=" + available +
                '}';
    }
}

