package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO Sprint add-controllers.
 */

@Service
public class ItemStorageImpl implements ItemStorage {

    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item addItem(Item item) {
        items.put(item.getId(), item);
        return items.get(item.getId());
    }

    @Override
    public Item partialUpdate(Map<String, Object> fields, Long userId, Long itemId) {
        Item item = items.get(itemId);
        for (String field : fields.keySet()) {
            if (field.equals("id")) {
                Integer id = (Integer) fields.get("id");
                Long newId = id.longValue();
                item.setId(newId);
            }
            if (field.equals("name")) {
                item.setName((String) fields.get("name"));
            }
            if (field.equals("description")) {
                item.setDescription((String) fields.get("description"));
            }
            if (field.equals("owner")) {
                Integer ownerId = (Integer) fields.get("owner");
                Long newOwnerId = ownerId.longValue();
                item.setOwner(newOwnerId);
            }
            if (field.equals("available")) {
                item.setAvailable((Boolean) fields.get("available"));
            }
        }
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
    public List<Item> getItems(List<Long> itemId) {
        List<Item> newItems = new ArrayList<>();
        for (Long id : itemId) {
            if (items.containsKey(id)) {
                newItems.add(items.get(id));
            }
        }
        return newItems;
    }

    @Override
    public void isItemExists(Long itemId) {
        if (!items.containsKey(itemId)) throw new EntityNotFoundException("Вещь не найдена");
    }
}
