package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestJoinAnswer;

public class ItemMapper {

    public static ItemDto getItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setOwner(item.getOwner());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setRequestId(item.getRequestId());
        itemDto.setAvailable(item.getAvailable());

        return itemDto;
    }

    public static Item getItem(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setRequestId(itemDto.getRequestId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setOwner(itemDto.getOwner());
        item.setAvailable(itemDto.getAvailable());

        return item;
    }

    public static ItemPublicDto getItemPublicDto(ItemRequestJoinAnswer requestJoinAnswer) {
        ItemPublicDto itemPublicDto = new  ItemPublicDto();
        itemPublicDto.setId(requestJoinAnswer.getItemId());
        itemPublicDto.setName(requestJoinAnswer.getItemName());
        itemPublicDto.setDescription(requestJoinAnswer.getItemDescription());
        itemPublicDto.setAvailable(requestJoinAnswer.getItemAvailable());
        itemPublicDto.setRequestId(requestJoinAnswer.getId());

        return itemPublicDto;
    }

    public static ItemPublicDto getItemPublicDtoFromItem(Item item) {
        ItemPublicDto itemPublicDto = new  ItemPublicDto();
        itemPublicDto.setId(item.getId());
        itemPublicDto.setName(item.getName());
        itemPublicDto.setDescription(item.getDescription());
        itemPublicDto.setAvailable(item.getAvailable());
        itemPublicDto.setRequestId(item.getRequestId());
        return itemPublicDto;
    }
}
