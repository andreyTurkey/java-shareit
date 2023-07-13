package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.ItemRequest;

import java.util.ArrayList;

public class ItemRequestMapper {

    public static ItemRequestDto getItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto.ItemRequestDtoBuilder()
                .id(itemRequest.getId())
                .created(itemRequest.getCreated())
                .description(itemRequest.getDescription())
                .userId(itemRequest.getUserId())
                .build();
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
       return ItemRequestPublicDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(new ArrayList<>())
                .build();
    }
}
