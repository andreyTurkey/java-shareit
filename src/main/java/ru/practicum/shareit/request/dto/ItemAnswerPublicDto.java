package ru.practicum.shareit.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
}













