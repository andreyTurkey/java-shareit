package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO Sprint add-controllers.
 */

@Slf4j
@Service
public class ItemStorageImpl implements ItemStorage {

    private final Map<Long, Item> items = new HashMap<>();

    private final Map<Long, List<Item>> userItems = new HashMap<>();

    private Long count = 0L;

    @Override
    public Item addItem(Item item) {
        count++;
        item.setId(count);
        items.put(item.getId(), item);
        addItemToUserList(item);
        return items.get(item.getId());
    }

    @Override
    public Item partialUpdate(ItemUpdateDto itemUpdateDto, Long userId, Long itemId) {
        Item item = items.get(itemId);
        if (itemUpdateDto.getName() != null) item.setName(itemUpdateDto.getName());
        if (itemUpdateDto.getDescription() != null) item.setDescription(itemUpdateDto.getDescription());
        if (itemUpdateDto.getAvailable() != null) item.setAvailable(itemUpdateDto.getAvailable());
        addItemToUserList(item);
        return item;
    }

    @Override
    public List<Item> getItemByParam(String text) {
        String lowerText = text.toLowerCase();
        List<Item> textItem = new ArrayList<>();
        for (Long id : items.keySet()) {
            if (items.get(id).getAvailable() && (items.get(id).getName().toLowerCase().contains(lowerText) ||
                    items.get(id).getDescription().toLowerCase().contains(lowerText))) {
                textItem.add(items.get(id));
            }
        }
        return textItem;
    }

    @Override
    public Item getItemById(Long id) {
        return items.get(id);
    }

    @Override
    public List<Item> getItems(Long userId) {
        return userItems.get(userId);
    }

    private List<Item> addItemToUserList(Item item) {
        Item oldItem = items.get(item.getId());
        List<Item> newUserItems = userItems.get(item.getOwner());
        if (newUserItems != null) {
            newUserItems.remove(oldItem);
            newUserItems.add(item);
        } else {
            newUserItems = new ArrayList<>();
            newUserItems.add(item);
        }
        userItems.put(item.getOwner(), newUserItems);
        return newUserItems;
    }
}
