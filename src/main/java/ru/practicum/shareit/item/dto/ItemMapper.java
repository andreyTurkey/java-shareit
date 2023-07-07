package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

    public static ItemDto getItemDto(Item item) {
        return new ItemDto.ItemDtoBuilder()
                .id(item.getId())
                .owner(item.getOwner())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item getItem(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setOwner(itemDto.getOwner());
        item.setAvailable(itemDto.getAvailable());

        return item;
    }
}
