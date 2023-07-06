package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;

public class GettingItem {

    // Ошибка: org.springframework.beans.factory.BeanDefinitionStoreException:
    // Failed to parse configuration class [ru.practicum.shareit.ShareItApp]; nested exception is
    // java.io.FileNotFoundException: class path resource [ru/practicum/shareit/item/dto/GettingItem.class]
    // cannot be opened because it does not exist

    ItemRepository itemRepository;

    //@Autowired
    public GettingItem(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    Item getItemById(Long itemId) {
        return itemRepository.getById(itemId);
    }
}
