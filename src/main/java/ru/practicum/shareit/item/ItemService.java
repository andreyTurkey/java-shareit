package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.CheckRentHistory;
import ru.practicum.shareit.booking.dto.BookingGetOwnerDto;
import ru.practicum.shareit.booking.dto.BookingMapperGetOwnerDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemService {

    final ItemRepository itemRepository;

    final UserService userService;

    //final UserRepository userRepository;

    final CheckRentHistory checkRentHistory;

    final CommentRepository commentRepository;

    final EntityManager em;

    public ItemDto getItemById(Long itemId) {
        return ItemMapper.getItemDto(itemRepository.getById(itemId));
    }

    public ItemDto addItem(ItemDto itemDto) {
        //existsById(itemDto.getOwner());
        userService.isUserExists(itemDto.getOwner());
        if (itemDto.getRequestId() == null) {
            itemDto.setRequestId(0L);
        }
        return ItemMapper.getItemDto(itemRepository.save(ItemMapper.getItem(itemDto)));
    }

    public ItemDto updateItem(ItemUpdateDto itemUpdateDto, Long userId, Long itemId) {
        //existsById(userId);
        userService.isUserExists(userId);
        Item item = itemRepository.getReferenceById(itemId);
        if (itemUpdateDto.getName() != null) {
            item.setName(itemUpdateDto.getName());
        }
        if (itemUpdateDto.getDescription() != null) {
            item.setDescription(itemUpdateDto.getDescription());
        }
        if (itemUpdateDto.getAvailable() != null) {
            item.setAvailable(itemUpdateDto.getAvailable());
        }
        return ItemMapper.getItemDto(itemRepository.save(item));
    }

    public ItemPublicDto findByIdAndOwner(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new EntityNotFoundException("Вещь не найдена"));

        List<CommentPublicDto> publicComments = commentRepository.findAllByItemId(itemId).stream()
                .map(CommentMapper::getPublicCommentDto)
                .collect(Collectors.toList());

        List<Booking> allBookingsByOwnerId = getAllBookingByOwnerIdAndItemId(userId, itemId);

        return ItemMapperGetOwnerDto.getPublicItemDto(item, userId, publicComments,
                getLastAndNextBooking(itemId, allBookingsByOwnerId));
    }

    public List<Booking> getAllBookingByOwnerIdAndItemId(Long ownerId, Long itemId) {
        List<Booking> bookings = em.createQuery(
                        "SELECT b from Booking b join Item i on b.item.id = i.id WHERE i.owner =:ownerId " +
                                " AND b.item.id =:itemId " +
                                " ORDER BY b.id DESC")
                .setParameter("ownerId", ownerId)
                .setParameter("itemId", itemId)
                .getResultList();
        return bookings;
    }

    public List<Booking> getAllBookingByOwnerId(Long ownerId) {
        List<Booking> bookings = em.createQuery(
                        "SELECT b from Booking b join Item i on b.item.id = i.id WHERE i.owner =:id ORDER BY b.id DESC")
                .setParameter("id", ownerId)
                .getResultList();
        return bookings;
    }

    public List<ItemPublicDto> findAllByUser(Long ownerId) {

        List<ItemPublicDto> convertedItems = new ArrayList<>();

        List<Item> itemsByOwner = itemRepository.findAllByOwner(ownerId);

        List<Booking> allBookingsAllItemsByOwner = getAllBookingByOwnerId(ownerId);

        List<CommentPublicDto> allCommentByItemByUser = commentRepository.findCommentByAllItemByUserId(ownerId).stream()
                .map(CommentMapper::getPublicCommentDto).collect(Collectors.toList());

        for (Item item : itemsByOwner) {
            List<BookingGetOwnerDto> getLastAndNextBooking = getLastAndNextBooking(item.getId(), allBookingsAllItemsByOwner);

            convertedItems.add(ItemMapperGetOwnerDto.getPublicItemDto(item, ownerId, allCommentByItemByUser,
                    getLastAndNextBooking));
        }
        return convertedItems;
    }

    private List<BookingGetOwnerDto> getLastAndNextBooking(Long itemId, List<Booking> allBookingByUser) {
        List<BookingGetOwnerDto> lastAndNextBooking = new ArrayList<>();

        List<Booking> allBookingByItemOrderEnd = allBookingByUser.stream().filter(booking -> {
                    return booking.getItem().getId().equals(itemId);
                })
                .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                .sorted(Comparator.comparing(Booking::getEnd).reversed()).collect(Collectors.toList());

        if (!(allBookingByItemOrderEnd.size() == 0) && !allBookingByItemOrderEnd.get(0).getStatus()
                .equals(BookingState.REJECTED)) {
            lastAndNextBooking.add(BookingMapperGetOwnerDto.getBookingGetOwnerDto(allBookingByItemOrderEnd.get(0)));
        } else {
            lastAndNextBooking.add(null);
        }

        List<Booking> allBookingByItemOrderStart = allBookingByUser.stream().filter(booking -> {
                    return booking.getItem().getId().equals(itemId);

                }).filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(Booking::getEnd)).collect(Collectors.toList());

        if (!(allBookingByItemOrderStart.size() == 0) && !allBookingByItemOrderStart.get(0).getStatus()
                .equals(BookingState.REJECTED)) {
            lastAndNextBooking.add(BookingMapperGetOwnerDto.getBookingGetOwnerDto(allBookingByItemOrderStart.get(0)));
        } else {
            lastAndNextBooking.add(null);
        }
        return lastAndNextBooking;
    }

    public List<ItemDto> getItemByParam(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text)
                .stream()
                .map(ItemMapper::getItemDto)
                .collect(Collectors.toList());
    }

    public CommentDto addComment(CommentAddDto commentAddDto) {
        existsById(commentAddDto.getUserId());
        checkRentHistory.isUserTookItem(commentAddDto);
        UserDto userDto = userService.getUserById(commentAddDto.getUserId());
        Item item = itemRepository.getReferenceById(commentAddDto.getItemId());
        return CommentMapper.getCommentDto(commentRepository.save(CommentMapper.getComment(
                CommentAddMapper.getCommentDto(commentAddDto, UserMapper.getUser(userDto), item))));
    }

    private void existsById(Long userId) {
        if (!userService.isUserExists(userId)) {
            throw new EntityNotFoundException("Проверьте ID = " + userId + " пользователя");
        }
    }

    public boolean existsByItemId(Long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new EntityNotFoundException("Вещь c ID =" + itemId + " не найдена.");
        }
        return true;
    }
}
