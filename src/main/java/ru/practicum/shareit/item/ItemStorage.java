package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface ItemStorage {

    Item addItem(Item item);

    Item getItemById(Long id);

    List<Item> getItems(List<Long> itemId);

    void isItemExists(Long itemId);

    Item partialUpdate(Map<String, Object> fields, Long userId, Long itemId);

    List<Item> getItemByParam(String text);
}
