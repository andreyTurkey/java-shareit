package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemPublicDto;
import ru.practicum.shareit.request.dto.*;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.user.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ItemRequestsService {

    final ItemRequestsRepository itemRequestsRepository;

    final ItemRepository itemRepository;

    final UserService userService;

    public ItemRequestDto addRequest(ItemRequestDto itemRequestDto) {
        userService.throwExceptionIfUserNotFound(itemRequestDto.getUserId());

        return ItemRequestMapper.getItemRequestDto(itemRequestsRepository.save(
                ItemRequestMapper.getItemRequest(itemRequestDto)));
    }

    public List<ItemRequestPublicDto> getAllRequestByUserId(Long userId) {
        userService.throwExceptionIfUserNotFound(userId);

        List<ItemRequest> allRequestsByUser = itemRequestsRepository.getItemRequestByUserId(userId);

        List<Long> requestIds = allRequestsByUser
                .stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList());

        Map<Long, List<ItemPublicDto>> itemDtosByRequestId = itemRepository.findAllByRequestIdIn(requestIds)
                .stream()
                .map(ItemMapper::getItemPublicDtoFromItem)
                .collect(Collectors.groupingBy(ItemPublicDto::getRequestId, Collectors.toList()));

        return allRequestsByUser.stream()
                .map(ItemRequestMapper::getItemRequestPublicDto)
                .map(itemRequestPublicDto -> {
                    itemRequestPublicDto.setItems(itemDtosByRequestId.getOrDefault(itemRequestPublicDto.getId(),
                            Collections.emptyList()));
                    return itemRequestPublicDto;
                })
                .collect(Collectors.toList());
    }

    public boolean requestExist(Long requestId) {
        if (!itemRequestsRepository.existsById(requestId)) {
            throw new EntityNotFoundException("Запрос не найден");
        }
        return true;
    }

    private ItemRequest getRequestById(Long requestId) {
        ItemRequest request = itemRequestsRepository.getReferenceById(requestId);
        return request;
    }

    public ItemRequestPublicDto getRequestById(Long requestId, Long userId) {
        userService.throwExceptionIfUserNotFound(userId);
        requestExist(requestId);

        ItemRequest request = getRequestById(requestId);

        List<ItemPublicDto> itemsByRequest = itemRepository.findAllByRequestId(requestId)
                .stream()
                .map(ItemMapper::getItemPublicDtoFromItem)
                .collect(Collectors.toList());

        ItemRequestPublicDto itemRequestPublicDto = new ItemRequestPublicDto();
        itemRequestPublicDto.setId(request.getId());
        itemRequestPublicDto.setDescription(request.getDescription());
        itemRequestPublicDto.setCreated(request.getCreated());
        itemRequestPublicDto.setItems(itemsByRequest);

        return itemRequestPublicDto;
    }

    public List<ItemRequestPublicDto> getAllRequestByPageable(Integer from, Integer size, Long userId) {
        userService.throwExceptionIfUserNotFound(userId);

        if (from == null && size == null) {
            return buildingRequests(null, userId);
        }

        Sort sortById = Sort.by(Sort.Direction.DESC, "created");
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size, sortById);

        return buildingRequests(page, userId);
    }

    private List<ItemRequestPublicDto> buildingRequests(Pageable page, Long userId) {
        List<ItemRequestPublicDto> itemRequestsByPageable;

        if (page == null) {
            itemRequestsByPageable = itemRequestsRepository.findAll()
                    .stream()
                    .filter(t -> (!t.getUserId().equals(userId)))
                    .map(ItemRequestMapper::getItemRequestPublicDto)
                    .collect(Collectors.toList());
        } else {
            itemRequestsByPageable = itemRequestsRepository.findAll(page).stream()
                    .filter(t -> (!t.getUserId().equals(userId)))
                    .map(ItemRequestMapper::getItemRequestPublicDto)
                    .collect(Collectors.toList());
        }

        List<Long> requestsId = itemRequestsByPageable.stream()
                .map(ItemRequestPublicDto::getId)
                .collect(Collectors.toList());

        Map<Long, List<ItemPublicDto>> itemDtosByRequestId = itemRepository.findAllByRequestIdIn(requestsId)
                .stream()
                .map(ItemMapper::getItemPublicDtoFromItem)
                .collect(Collectors.groupingBy(ItemPublicDto::getRequestId, Collectors.toList()));

        return itemRequestsByPageable.stream()
                .map(itemRequestPublicDto -> {
                    itemRequestPublicDto.setItems(itemDtosByRequestId.getOrDefault(itemRequestPublicDto.getId(),
                            Collections.emptyList()));
                    return itemRequestPublicDto;
                })
                .collect(Collectors.toList());
    }
}
