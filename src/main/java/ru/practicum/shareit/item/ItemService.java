package ru.practicum.shareit.item;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
@Component
public class ItemService {

    private final ItemStorage itemStorage;

    private final UserStorage userStorage;

    private final ItemMapper itemMapper;

    private Long count;

    @Autowired
    public ItemService(@Qualifier("ItemStorage") ItemStorage itemStorage,
                       UserStorage userStorage, ItemMapper itemMapper) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
        this.itemMapper = itemMapper;
        count = 1L;
    }

    public Item addItem(Item item) {
        userStorage.isUserExistById(item.getOwner());
        item.setId(count);
        count++;
        itemStorage.addItem(item);
        User user = userStorage.getUserById(item.getOwner());
        user.addItems(item);
        return itemStorage.getItemById(item.getId());
    }

    public Item updateItem(Map<String, Object> fields, Long userId, Long itemId) {
        validationItem(itemId, userId);
        itemStorage.partialUpdate(fields, userId, itemId);
        return itemStorage.partialUpdate(fields, userId, itemId);
    }

    public Item getItemById(Long itemId, Long userId) {
        itemStorage.isItemExists(itemId);
        userStorage.isUserExistById(userId);
        return itemStorage.getItemById(itemId);
    }

    public List<Item> getAllItemByUserId(Long userId) {
        userStorage.isUserExistById(userId);
        User user = userStorage.getUserById(userId);
        List<Long> itemId = user.getItems();
        return itemStorage.getItems(itemId);
    }

    private void validationItem(Long itemId, Long userId) {
        itemStorage.isItemExists(itemId);
        userStorage.isUserExistById(userId);
        if (itemStorage.getItemById(itemId).getOwner() != userId)
            throw new UserNotFoundException("Пользователь не является владельцем вещи.");
    }

    public List<Item> getItemByParam(String text) {
        if (text.isBlank()) return new ArrayList<>();
        return itemStorage.getItemByParam(text);
    }

    public ItemDto getItemDto(Item item) {
        return itemMapper.getItemDto(item);
    }
}
