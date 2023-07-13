package ru.practicum.shareit.request.dto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class ItemRequestJoinAnswer {

    Long id; //есть

    String description; // есть

    Long userId;

    LocalDateTime created; // есть

    Long itemId; //есть

    String itemName; //есть

    String itemDescription; //есть

    Boolean itemAvailable; //есть
}
