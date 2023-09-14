package ru.practicum.shareit.item;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
@ToString
public class CommentPublicDto {

    Long id;

    String text;

    String authorName;

    LocalDateTime created;
}
