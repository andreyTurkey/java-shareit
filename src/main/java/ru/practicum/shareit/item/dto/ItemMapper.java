package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {

    public ItemDto getItemDto(Item item) {
        return new ItemDto.ItemDtoBuilder()
                .id(item.getId())
                .owner(item.getOwner())
                .name(item.getName())
                .comments(item.getComments())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public Item getItem(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setOwner(itemDto.getOwner());
        item.setComments(itemDto.getComments());
        item.setAvailable(itemDto.getAvailable());

        return item;
    }
}
