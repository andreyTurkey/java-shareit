package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {

    public ItemDto getItemDto(Item item) {
        return new ItemDto.ItemDtoBuilder()
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable()).build();
    }
}
