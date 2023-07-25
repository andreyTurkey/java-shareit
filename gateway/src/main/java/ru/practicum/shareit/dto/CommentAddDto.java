package ru.practicum.shareit.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class CommentAddDto {

    Long id;

    Long itemId;

    Long userId;

    @NotBlank
    String text;

    LocalDateTime created;
}
