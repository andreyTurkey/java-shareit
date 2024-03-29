package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;

@Validated
@Slf4j
@AllArgsConstructor
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping(path = "/requests")
public class ItemRequestController {

    final ItemRequestClient itemRequestClient;

    static final String USER_FROM_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                          @RequestHeader(value = USER_FROM_HEADER) Long userId) {
        return itemRequestClient.addRequest(userId, itemRequestDto);
    }

    @GetMapping(value = "/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader(value = USER_FROM_HEADER) Long userId,
                                               @PathVariable Long requestId) {
        return itemRequestClient.getRequestById(requestId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllRequestByUserId(@RequestHeader(value = USER_FROM_HEADER) Long userId) {
        return itemRequestClient.getAllRequestByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getRequestByParam(@RequestHeader(value = USER_FROM_HEADER) Long userId, @PositiveOrZero
                                                        @RequestParam(value = "from", required = false) Integer from,
                                                        @RequestParam(value = "size", required = false) Integer size) {
        return itemRequestClient.getAllRequestByPageable(from, size, userId);
    }
}
