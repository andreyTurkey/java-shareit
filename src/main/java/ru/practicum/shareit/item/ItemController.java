package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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
    public Item addItem(@Valid @RequestBody Item item, @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.debug(item + " - получен запрос на добавление вещи");
        item.setOwner(userId);
        itemService.addItem(item);
        log.debug(item + " - вещь была добавлена");
        return item;
    }

    @RequestMapping(value = "/{itemId}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Item partialUpdate(
            @RequestBody Map<String, Object> updates,
            @PathVariable("itemId") Long itemId,
            @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.debug("Пользователь с ID = {} был обновлен", userId);
        return itemService.updateItem(updates, userId, itemId);
    }

    @GetMapping(value = "/{itemId}")
    public Item getItemById(@PathVariable("itemId") Long itemId, @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.debug("Получен запрос вещи с  ID = {} ", itemId);
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<Item> getAllItemByUserId(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.debug("Получен запрос листа вещей пользователя с  ID = {} ", userId);
        return itemService.getAllItemByUserId(userId);
    }

    @GetMapping("/search")
    public List<Item> getItemByParam(@RequestParam(value = "text") String text) {
        log.debug("Получен запрос на поиск вещи с текстом = {} ", text);
        return itemService.getItemByParam(text);
    }
}
