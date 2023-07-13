package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ItemDto {

    Long id;

    @NotBlank(message = "NAME can't be empty.")
    String name;

    @NotNull(message = "DESCRIPTION can't be empty.")
    String description;

    Long owner;

    @NotNull
    Boolean available;

    Long requestId;
/*
    public ItemDto(Long id, String name, String description, Long owner, Boolean available, Long requestId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.available = available;
        this.requestId = requestId;
    }*/
}

