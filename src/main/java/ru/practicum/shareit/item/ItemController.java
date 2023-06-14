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
        item.setOwner(userId);
        itemService.addItem(item);
        return item;
    }

    @RequestMapping(value = "/{itemId}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Item partialUpdate(
            @RequestBody Map<String, Object> updates,
            @PathVariable("itemId") Long itemId,
            @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.error(updates + "входящие поля");
        return itemService.updateItem(updates, userId, itemId);
    }

    @GetMapping(value = "/{itemId}")
    public Item getItemById(@PathVariable("itemId") Long itemId, @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<Item> getAllItemByUserId(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        return itemService.getAllItemByUserId(userId);
    }

    @GetMapping("/search")
    public List<Item> getItemByParam(@RequestParam(value = "text") String text) {
        return itemService.getItemByParam(text);
    }
}
