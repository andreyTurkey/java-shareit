package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {

    final ItemServiceDB itemService;

    @Autowired
    public ItemController(ItemServiceDB itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto addItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.debug(itemDto + " - получен запрос на добавление вещи");
        itemDto.setOwner(userId);
        log.debug(itemDto + " - вещь была добавлена");
        return itemService.addItem(itemDto);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDto partialUpdate(
            @RequestBody ItemUpdateDto itemUpdateDto,
            @PathVariable("itemId") Long itemId,
            @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.debug("Вещь с ID = {} был обновлен", itemId);
        return itemService.updateItem(itemUpdateDto, userId, itemId);
    }

    @GetMapping(value = "/{itemId}")
    public ItemPublicDto getItemByIdByUserId(
            @PathVariable("itemId") Long itemId,
            @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.debug("Вещь с ID = {} была запрошена", itemId);
        return itemService.findByIdAndOwner(itemId, userId);
    }

    @GetMapping
    public List<ItemPublicDto> getAllByOwner(@RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.debug("Запрос всех вещей пользователя ID = {}", userId);
        return itemService.findAllByOwner(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemByParam(@RequestParam(value = "text") String text) {
        log.debug("Получен запрос на поиск вещи с текстом = {} ", text);
        return itemService.getItemByParam(text);
    }

    @PostMapping(value = "/{itemId}/comment")
    public CommentDto addComment(@PathVariable("itemId") Long itemId, @Valid @RequestBody CommentAddDto commentAddDto,
                                 @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.debug(commentAddDto + " - получен запрос на добавление отзыва");
        commentAddDto.setUserId(userId);
        commentAddDto.setItemId(itemId);
        commentAddDto.setCreated(LocalDateTime.now());
        log.debug(commentAddDto + " - отзыв был добавлен");
        return itemService.addComment(commentAddDto);
    }
}
