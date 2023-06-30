package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemServiceDB {

    ItemDto addItem(ItemDto itemDto);

    ItemDto updateItem(ItemUpdateDto itemUpdateDto, Long userId, Long itemId);

    ItemPublicDto findByIdAndOwner(Long itemId, Long userId);

    List<ItemPublicDto> findAllByOwner(Long userId);

    List<ItemDto> getItemByParam(String text);

    ItemDto getItemById(Long itemId);

    CommentDto addComment(CommentAddDto commentAddDto);
}
