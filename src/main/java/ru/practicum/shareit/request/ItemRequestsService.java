package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemPublicDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.*;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.user.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
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

    final EntityManager em;

    public ItemRequestDto addRequest(ItemRequestDto itemRequestDto) {
        userService.userExists(itemRequestDto.getUserId());

        return ItemRequestMapper.getItemRequestDto(itemRequestsRepository.save(ItemRequestMapper.getItemRequest(itemRequestDto)));
    }

    public List<ItemRequestPublicDto> getAllRequestByUserId(Long userId) {
        userService.userExists(userId);

        Set<ItemRequestPublicDto> requests = new HashSet<>();

        Set<ItemPublicDto> items = new HashSet<>();

        List<ItemRequestJoinAnswer> requestJoinAnswers = getAllRequestByUserIdJoinAnswer(userId); // получен JOIN

        for (ItemRequestJoinAnswer requestJoinAnswer : requestJoinAnswers) { // Собраны REQUEST-ы
            ItemRequestPublicDto itemRequestPublicDto = ItemRequestPublicDto.builder()
                    .id(requestJoinAnswer.getId())
                    .description(requestJoinAnswer.getDescription())
                    .created(requestJoinAnswer.getCreated())
                    .items(new ArrayList<>())
                    .build();
            requests.add(itemRequestPublicDto);
        }

        for (ItemRequestJoinAnswer requestJoinAnswer : requestJoinAnswers) {
            if (requestJoinAnswer.getItemId() == null) {
                break;
            } else {
                items.add(ItemMapper.getItemPublicDto(requestJoinAnswer));
            }
        }

        for (ItemRequestPublicDto requestPublicDto : requests) { // сборка REQUEST - ов
            for (ItemPublicDto item : items) {
                if (requestPublicDto.getId().equals(item.getRequestId())) {
                    requestPublicDto.getItems().add(item);
                }
            }
        }
        return new ArrayList<>(requests);
    }

    public List<ItemRequestJoinAnswer> getAllRequestByUserIdJoinAnswer(Long userId) {
        List<ItemRequestJoinAnswer> requests = em.createQuery(
                        "SELECT new ru.practicum.shareit.request.dto.ItemRequestJoinAnswer (" +
                                "ir.id as id, " +
                                "ir.description as description, " +
                                "ir.userId as userId, " +
                                "ir.created as created, " +
                                "it.id as itemId, " +
                                "it.name as itemName, " +
                                "it.description as itemDescription, " +
                                "it.available as itemAvailable) " +
                                "from ItemRequest ir " +
                                "LEFT JOIN Item it on ir.id = it.requestId WHERE ir.userId =:id ORDER BY ir.id DESC")
                .setParameter("id", userId)
                .getResultList();

        return requests;
    }

    public List<ItemRequestJoinAnswer> getRequestByIdJoinAnswer(Long requestId) {
        List<ItemRequestJoinAnswer> requests = em.createQuery(
                        "SELECT new ru.practicum.shareit.request.dto.ItemRequestJoinAnswer (" +
                                "ir.id as id, " +
                                "ir.description as description, " +
                                "ir.userId as userId, " +
                                "ir.created as created, " +
                                "it.id as itemId, " +
                                "it.name as itemName, " +
                                "it.description as itemDescription, " +
                                "it.available as itemAvailable) " +
                                "from ItemRequest ir " +
                                "LEFT JOIN Item it on ir.id = it.requestId WHERE ir.id =:id ORDER BY ir.id DESC")
                .setParameter("id", requestId)
                .getResultList();

        return requests;
    }

    public void requestExist(Long requestId) {
        if (!itemRequestsRepository.existsById(requestId)) {
            throw new EntityNotFoundException("Запрос не найден");
        }
    }

    public ItemRequestPublicDto getRequestById(Long requestId, Long userId) {
        userService.userExists(userId);

        requestExist(requestId);

        ItemRequestJoinAnswer itemRequestJoinAnswer = null;

        Set<ItemPublicDto> items = new HashSet<>();

        List<ItemRequestJoinAnswer> requestJoinAnswersForAnswer = getRequestByIdJoinAnswer(requestId);

        Optional<ItemRequestJoinAnswer> requestJoinAnswers = getRequestByIdJoinAnswer(requestId)
                .stream().limit(1).findFirst();

        if (requestJoinAnswers.isPresent()) {
            itemRequestJoinAnswer = requestJoinAnswers.get();
        }

        ItemRequestPublicDto itemRequestPublicDto = ItemRequestPublicDto.builder()
                .id(itemRequestJoinAnswer.getId())
                .description(itemRequestJoinAnswer.getDescription())
                .created(itemRequestJoinAnswer.getCreated())
                .items(new ArrayList<>())
                .build();

        for (ItemRequestJoinAnswer requestJoinAnswer : requestJoinAnswersForAnswer) {
            if (requestJoinAnswer.getItemId() == null) {
                break;
            } else {
                items.add(ItemMapper.getItemPublicDto(requestJoinAnswer));
            }
        }
        itemRequestPublicDto.setItems(new ArrayList<>(items));

        return itemRequestPublicDto;
    }

    public List<ItemRequestPublicDto> getAllRequestByPageable(Integer from, Integer size, Long userId) {
        userService.userExists(userId);

        if (from == null && size == null) {
            return buildingRequests(null, userId);
        }

        if (from < 0 || size <= 0) {
            throw new NotAvailableException("Проверьте параметры запроса");
        }

        Sort sortById = Sort.by(Sort.Direction.DESC, "id");
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size, sortById);

        return buildingRequests(page, userId);
    }

    public List<ItemRequestPublicDto> buildingRequests(Pageable page, Long userId) {
        List<ItemRequestPublicDto> itemRequestsByPageable;
        if (page == null) {
            itemRequestsByPageable = itemRequestsRepository.findAll().stream().filter(t -> {
                return (!t.getUserId().equals(userId));
            }).map(ItemRequestMapper::getItemRequestPublicDto).collect(Collectors.toList());
        } else {
            itemRequestsByPageable = itemRequestsRepository.findAll(page).filter(t -> {
                return (!t.getUserId().equals(userId));
            }).map(ItemRequestMapper::getItemRequestPublicDto).toList();
        }

        List<Long> requestsId = itemRequestsByPageable.stream().map(t -> {
            return t.getId();
        }).collect(Collectors.toList());

        List<Item> findAllByList = itemRepository.findAllByRequestId(requestsId);

        for (ItemRequestPublicDto request : itemRequestsByPageable) {
            for (Item item : findAllByList) {
                if (request.getId().equals(item.getRequestId())) {
                    request.getItems().add(ItemMapper.getItemPublicDtoFromItem(item));
                }
            }
        }
        return itemRequestsByPageable;
    }
}
