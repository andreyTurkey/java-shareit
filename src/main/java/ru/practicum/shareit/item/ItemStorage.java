package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    Item addItem(Item item);

    Item getItemById(Long id);

    List<Item> getItems(Long userId);

    Item partialUpdate(ItemUpdateDto fields, Long userId, Long itemId);

    List<Item> getItemByParam(String text);
}
