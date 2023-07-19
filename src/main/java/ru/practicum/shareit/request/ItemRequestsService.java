package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemPublicDto;
import ru.practicum.shareit.item.model.Item;
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
        userService.isUserExists(itemRequestDto.getUserId());

        return ItemRequestMapper.getItemRequestDto(itemRequestsRepository.save(ItemRequestMapper.getItemRequest(itemRequestDto)));
    }

    public List<ItemRequestPublicDto> getAllRequestByUserId(Long userId) {
        userService.isUserExists(userId);

        List<ItemRequest> allRequestsByUser = itemRequestsRepository.getItemRequestByUserId(userId);

        List<Item> allItemsByRequests = itemRepository.findAllByRequestIdIn(allRequestsByUser
                .stream()
                .map(t -> t.getId())
                .collect(Collectors.toList()));

        Set<ItemRequestPublicDto> requests = new HashSet<>();

        Map<Long, List<ItemPublicDto>> newRequests = new HashMap<>();

        for (ItemRequest requestJoinAnswer : allRequestsByUser) {

            ItemRequestPublicDto itemRequestPublicDto = new ItemRequestPublicDto();
            itemRequestPublicDto.setId(requestJoinAnswer.getId());
            itemRequestPublicDto.setDescription(requestJoinAnswer.getDescription());
            itemRequestPublicDto.setCreated(requestJoinAnswer.getCreated());
            itemRequestPublicDto.setItems(new ArrayList<>());

            newRequests.put(itemRequestPublicDto.getId(), new ArrayList<>());
            requests.add(itemRequestPublicDto);
        }

        for (Item requestAnswer : allItemsByRequests) {
            if (newRequests.containsKey(requestAnswer.getRequestId())) {
                ItemPublicDto newItemPublicDto = ItemMapper.getItemPublicDtoFromItem(requestAnswer);
                newRequests.get(requestAnswer.getRequestId()).add(newItemPublicDto);
            }
        }

        for (ItemRequestPublicDto requestPublicDto : requests) {
            if (newRequests.containsKey(requestPublicDto.getId())) {
                requestPublicDto.setItems(newRequests.get(requestPublicDto.getId()));
            }
        }
        return new ArrayList<>(requests);
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
        userService.isUserExists(userId);
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
        userService.isUserExists(userId);

        if (from == null && size == null) {
            return buildingRequests(null, userId);
        }

        Sort sortById = Sort.by(Sort.Direction.DESC, "created");
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size, sortById);

        return buildingRequests(page, userId);
    }

    private List<ItemRequestPublicDto> buildingRequests(Pageable page, Long userId) {
        List<ItemRequestPublicDto> itemRequestsByPageable;

        Map<Long, List<Item>> newRequests = new HashMap<>();

        if (page == null) {
            itemRequestsByPageable = itemRequestsRepository.findAll()
                    .stream()
                    .filter(t -> (!t.getUserId().equals(userId)))
                    .map(ItemRequestMapper::getItemRequestPublicDto)
                    .collect(Collectors.toList());
            for (ItemRequestPublicDto request : itemRequestsByPageable) {
                newRequests.put(request.getId(), new ArrayList<>());
            }
        } else {
            itemRequestsByPageable = itemRequestsRepository.findAll(page)
                    .stream()
                    .filter(t -> (!t.getUserId().equals(userId)))
                    .map(ItemRequestMapper::getItemRequestPublicDto)
                    .collect(Collectors.toList());
            for (ItemRequestPublicDto request : itemRequestsByPageable) {
                newRequests.put(request.getId(), new ArrayList<>());
            }
        }
        List<Long> requestsId = itemRequestsByPageable.stream()
                .map(t -> t.getId())
                .collect(Collectors.toList());

        List<Item> findAllByList = itemRepository.findAllByRequestIdIn(requestsId);

        for (Item newItem : findAllByList) {
            if (newRequests.containsKey(newItem.getRequestId())) {
                newRequests.get(newItem.getRequestId()).add(newItem);
            }
        }

        for (ItemRequestPublicDto newRequest : itemRequestsByPageable) {
            if (newRequests.containsKey(newRequest.getId())) {
                newRequest.setItems(newRequests.get(newRequest.getId())
                        .stream()
                        .map(ItemMapper::getItemPublicDtoFromItem)
                        .collect(Collectors.toList())
                );
            }
        }
        return itemRequestsByPageable;
    }
}
