package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@ToString
public class CommentDto {

    Long id;

    @NotNull
    Item item;

    User user;

    @NotNull
    String text;

    @NotNull
    String authorName;

    LocalDateTime created;
}
