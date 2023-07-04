package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;

public class GettingItem {

    ItemRepository itemRepository;

    //@Autowired
    public GettingItem(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    Item getItemById(Long itemId) {
        return itemRepository.getById(itemId);
    }
}
