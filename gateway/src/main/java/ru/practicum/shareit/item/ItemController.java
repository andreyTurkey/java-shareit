package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;

    private static final String USER_FROM_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader(value = USER_FROM_HEADER) Long userId) {
        return itemClient.addItem(userId, itemDto);
    }

    @PatchMapping(value = "/{itemId}")
    public ResponseEntity<Object> partialUpdate(
            @RequestBody ItemUpdateDto itemUpdateDto,
            @Positive @PathVariable("itemId") Long itemId,
            @Positive @RequestHeader(value = USER_FROM_HEADER) Long userId) {
        return itemClient.updateItem(itemUpdateDto, userId, itemId);
    }

    @GetMapping(value = "/{itemId}")
    public ResponseEntity<Object> getItemByIdByUserId(@Positive @PathVariable("itemId") Long itemId,
                                             @Positive @RequestHeader(value = USER_FROM_HEADER) Long userId) {
        return itemClient.findByIdAndOwner(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByUser(@Positive @RequestHeader(value = USER_FROM_HEADER) Long userId) {
        return itemClient.findAllByUser(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemByParam(@RequestParam(value = "text") String text) {
        return itemClient.getItemByParam(text);
    }

    @PostMapping(value = "/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Positive @PathVariable("itemId") Long itemId, @Valid @RequestBody CommentAddDto commentAddDto,
                                 @Positive @RequestHeader(value = USER_FROM_HEADER) Long userId) {
        return itemClient.addComment(itemId, userId, commentAddDto);
    }
}
