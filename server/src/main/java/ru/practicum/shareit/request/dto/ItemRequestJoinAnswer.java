package ru.practicum.shareit.request.dto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
public class ItemRequestJoinAnswer {

    Long id;

    String description;

    Long userId;

    LocalDateTime created;

    Long itemId;

    String itemName;

    String itemDescription;

    Boolean itemAvailable;
}
