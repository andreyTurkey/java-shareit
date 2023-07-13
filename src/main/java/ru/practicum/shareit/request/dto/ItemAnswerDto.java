package ru.practicum.shareit.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemAnswerDto {

    Long id;

    Long itemId;

    Long itemRequestId;

    //список ответов в формате: id вещи, название,
    // её описание description, а также requestId запроса и признак доступности вещи available.
}
