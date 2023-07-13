package ru.practicum.shareit.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.dto.ItemPublicDto;

@Builder
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemAnswerPublicDto {

    Long itemId;

    String itemName;

    String itemDescription;

    Boolean itemAvailable;

    Long itemRequestId;

    //список ответов в формате: id вещи, название,
    // её описание description, а также requestId запроса и признак доступности вещи available.
}













