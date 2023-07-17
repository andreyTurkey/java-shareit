package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.ItemRequest;

import java.util.ArrayList;

public class ItemRequestMapper {

    public static ItemRequestDto getItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setUserId(itemRequest.getUserId());

        return itemRequestDto;
    }

    public static ItemRequest getItemRequest(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(itemRequestDto.getId());
        itemRequest.setCreated(itemRequestDto.getCreated());
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setUserId(itemRequestDto.getUserId());

        return itemRequest;
    }

    public static ItemRequestPublicDto getItemRequestPublicDto(ItemRequest itemRequest) {
        ItemRequestPublicDto itemRequestPublicDto = new ItemRequestPublicDto();
        itemRequestPublicDto.setId(itemRequest.getId());
        itemRequestPublicDto.setDescription(itemRequest.getDescription());
        itemRequestPublicDto.setCreated(itemRequest.getCreated());
        itemRequestPublicDto.setItems(new ArrayList<>());

        return itemRequestPublicDto;
    }
}
