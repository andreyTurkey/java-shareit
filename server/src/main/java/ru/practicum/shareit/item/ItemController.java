package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    private static final String USER_FROM_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto addItem(@RequestBody ItemDto itemDto, @RequestHeader(value = USER_FROM_HEADER) Long userId) {
        log.debug(itemDto + " - получен запрос на добавление вещи");
        itemDto.setOwner(userId);
        return itemService.addItem(itemDto);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDto partialUpdate(
            @RequestBody ItemUpdateDto itemUpdateDto,
            @PathVariable("itemId") Long itemId,
            @RequestHeader(value = USER_FROM_HEADER) Long userId) {
        log.debug("Вещь с ID = {} был обновлен", itemId);
        return itemService.updateItem(itemUpdateDto, userId, itemId);
    }

    @GetMapping(value = "/{itemId}")
    public ItemPublicDto getItemByIdByUserId(@PathVariable("itemId") Long itemId,
            @RequestHeader(value = USER_FROM_HEADER) Long userId) {
        log.debug("Вещь с ID = {} была запрошена", itemId);
        return itemService.findByIdAndOwner(itemId, userId);
    }

    @GetMapping
    public List<ItemPublicDto> getAllByUser(@RequestHeader(value = USER_FROM_HEADER) Long userId) {
        log.debug("Запрос всех вещей пользователя ID = {}", userId);
        return itemService.findAllByUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemByParam(@RequestParam(value = "text") String text) {
        log.debug("Получен запрос на поиск вещи с текстом = {} ", text);
        return itemService.getItemByParam(text);
    }

    @PostMapping(value = "/{itemId}/comment")
    public CommentDto addComment(@PathVariable("itemId") Long itemId, @RequestBody CommentAddDto commentAddDto,
                                 @RequestHeader(value = USER_FROM_HEADER) Long userId) {
        log.debug(commentAddDto + " - получен запрос на добавление отзыва");
        commentAddDto.setUserId(userId);
        commentAddDto.setItemId(itemId);
        commentAddDto.setCreated(LocalDateTime.now());
        return itemService.addComment(commentAddDto);
    }
}
