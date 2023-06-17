package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ItemService {

    private final ItemStorage itemStorage;

    private final UserStorage userStorage;

    private final ItemMapper itemMapper;


    @Autowired
    public ItemService(ItemStorage itemStorage,
                       UserStorage userStorage, ItemMapper itemMapper) {
        this.itemStorage = itemStorage;
        this.userStorage = userStorage;
        this.itemMapper = itemMapper;
    }

    public ItemDto addItem(ItemDto itemDto) {
        userStorage.isUserExistById(itemDto.getOwner());
        Item item = itemMapper.getItem(itemDto);
        itemStorage.addItem(item);
        return itemMapper.getItemDto(itemStorage.getItemById(item.getId()));
    }

    public ItemDto updateItem(ItemUpdateDto itemUpdateDto, Long userId, Long itemId) {
        validationItem(itemId, userId);
        Item itemDto = itemStorage.partialUpdate(itemUpdateDto, userId, itemId);
        return itemMapper.getItemDto(itemDto);
    }

    public ItemDto getItemById(Long itemId) {
        return itemMapper.getItemDto(itemStorage.getItemById(itemId));
    }

    public List<ItemDto> getAllItemByUserId(Long userId) {
        return itemStorage.getItems(userId).stream()
                .map(itemMapper::getItemDto).collect(Collectors.toList());
    }

    private void validationItem(Long itemId, Long userId) {
        Long ownerId = itemStorage.getItemById(itemId).getOwner();
        if (!Objects.equals(ownerId, userId))
            throw new EntityNotFoundException("Пользователь не является владельцем вещи.");
    }

    public List<ItemDto> getItemByParam(String text) {
        if (text.isBlank()) return new ArrayList<>();
        return itemStorage.getItemByParam(text).stream()
                .map(itemMapper::getItemDto).collect(Collectors.toList());
    }
}
