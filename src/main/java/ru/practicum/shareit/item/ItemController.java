package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {

    final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto addItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.debug(itemDto + " - получен запрос на добавление вещи");
        itemDto.setOwner(userId);
        log.debug(itemDto + " - вещь была добавлена");
        return itemService.addItem(itemDto);
    }

    @RequestMapping(value = "/{itemId}")
    public ItemDto partialUpdate(
            @RequestBody ItemUpdateDto itemUpdateDto,
            @PathVariable("itemId") Long itemId,
            @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.debug("Пользователь с ID = {} был обновлен", userId);
        return itemService.updateItem(itemUpdateDto, userId, itemId);
    }

    @GetMapping(value = "/{itemId}")
    public ItemDto getItemById(@PathVariable("itemId") Long itemId) {
        log.debug("Получен запрос вещи с  ID = {} ", itemId);
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItemByUserId(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.debug("Получен запрос листа вещей пользователя с  ID = {} ", userId);
        return itemService.getAllItemByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemByParam(@RequestParam(value = "text") String text) {
        log.debug("Получен запрос на поиск вещи с текстом = {} ", text);
        return itemService.getItemByParam(text);
    }
}
