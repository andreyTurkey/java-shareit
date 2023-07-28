package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestPublicDto;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Validated
@Slf4j
@AllArgsConstructor
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping(path = "/requests")
public class ItemRequestController {

    final ItemRequestsService itemRequestsService;

    static final String USER_FROM_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto addRequest(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                  @RequestHeader(value = USER_FROM_HEADER) Long userId) {
        log.debug(itemRequestDto + " - получен запрос на  добавление");
        itemRequestDto.setUserId(userId);
        itemRequestDto.setCreated(LocalDateTime.now());
        return itemRequestsService.addRequest(itemRequestDto);
    }

    @GetMapping(value = "/{requestId}")
    public ItemRequestPublicDto getRequestById(@RequestHeader(value = USER_FROM_HEADER) Long userId,
                                               @PathVariable Long requestId) {
        log.debug("Получен запрос  запроса с  ID = {} ", requestId);
        return itemRequestsService.getRequestById(requestId, userId);
    }

    @GetMapping
    public List<ItemRequestPublicDto> getAllRequestByUserId(@RequestHeader(value = USER_FROM_HEADER) Long userId) {
        log.debug("Получен запрос всех пользователей");
        return itemRequestsService.getAllRequestByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestPublicDto> getRequestByParam(@RequestHeader(value = USER_FROM_HEADER) Long userId, @PositiveOrZero
                                                        @RequestParam(value = "from", required = false) Integer from,
                                                        @RequestParam(value = "size", required = false) Integer size) {
        log.debug("Получен постраничный запрос запросов ");
        return itemRequestsService.getAllRequestByPageable(from, size, userId);
    }
}
