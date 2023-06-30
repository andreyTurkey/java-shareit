package ru.practicum.shareit.item.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;

@Slf4j
@Component
public class GettingItem {

    ItemRepository itemRepository;

    @Autowired
    public GettingItem(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    Item getItemById(Long itemId) {
        return itemRepository.getById(itemId);
    }
}
