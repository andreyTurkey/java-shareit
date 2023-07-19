package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingServiceImpl implements BookingService {

    BookingRepository bookingRepository;

    ItemService itemService;

    UserService userService;

    @Override
    public BookingDto addBooking(BookingAddDto bookingAddDto)  {
        UserDto userDto = userService.getUserById(bookingAddDto.getUserId());
        ItemDto itemDto = itemService.getItemById(bookingAddDto.getItemId());
        BookingDto bookingDto = BookingAddMapper.getBookingDto(bookingAddDto, itemDto, UserMapper.getUser(userDto));
        if (bookingDto.getItem().getOwner().equals(bookingAddDto.getUserId())) {
            throw new EntityNotFoundException("Владелец вещи не может создавать бронирование");
        }
        bookingDto.setStatus(BookingState.WAITING);
        isAvailable(ItemMapper.getItem(bookingDto.getItem()));

        List<Booking> allBookingByItem = bookingRepository.findAllByItem_Id(bookingDto.getItem().getId())
                .stream()
                .filter(booking -> booking.getStart().isBefore(bookingDto.getStart())
                            && booking.getEnd().isAfter(bookingDto.getStart()))
                .collect(Collectors.toList());

        if (bookingDto.getStart().isAfter(bookingDto.getEnd()) || bookingDto.getStart().equals(bookingDto.getEnd())
                || bookingDto.getStart().isBefore(LocalDateTime.now()) || allBookingByItem.size() != 0) {
            throw new NotAvailableException("Проверьте даты бронирования");
        }
        existsById(bookingDto);

        bookingAddDto.setStatus(BookingState.WAITING);
        return BookingMapper.getBookingDto(bookingRepository.save(BookingMapper.getBooking(bookingDto)));
    }

    @Override
    public BookingDto updateStatus(Long bookerId, Long bookingId, Boolean approved) {
        final Boolean APPROVE_STATUS = true;
        Booking booking = bookingRepository.getReferenceById(bookingId);
        if (!(booking.getItem().getOwner().equals(bookerId))) {
            throw new EntityNotFoundException("Проверьте ID = " + bookerId + " пользователя");
        }
        if (APPROVE_STATUS.equals(approved)) {
            if (!booking.getStatus().equals(BookingState.WAITING)) {
                throw new NotAvailableException("Бронирование уже подтверждено");
            }
            booking.setStatus(BookingState.APPROVED);
        } else {
            if (booking.getStatus().equals(BookingState.APPROVED)) {
                throw new NotAvailableException("Бронирование уже подтверждено");
            } else {
                booking.setStatus(BookingState.REJECTED);
            }
        }
        return BookingMapper.getBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long bookerId, Long ownerId) {
        Booking booking = bookingRepository.getReferenceById(bookingId);
        if (booking.getItem().getOwner().equals(ownerId) || booking.getUser().getId().equals(bookerId)) {
            return BookingMapper.getBookingDto(booking);
        } else {
            throw new EntityNotFoundException("Проверьте ID = " + bookerId + " пользователя");
        }
    }

    @Override
    public List<BookingDto> getAllBookingsByUserId(Long userId, String state, Integer from, Integer size) {
        userExistsById(userId);

        if (from == null && size == null) {
            return getAllBookingsByUserIdWithoutPageable(null, userId, state);
        } else {
            Sort sortById = Sort.by(Sort.Direction.DESC, "id");
            Pageable page = PageRequest.of(from > 0 ? from / size : 0, size, sortById);
            return getAllBookingsByUserId(userId, page).stream().map(BookingMapper::getBookingDto)
                    .collect(Collectors.toList());
        }
    }

    private List<Booking> getAllBookingsByUserId(Long userId, Pageable page) {
        List<Long> bookingsIds = bookingRepository.findBookingByUserId(userId, page)
                .getContent()
                .stream()
                .map(t -> t.getId())
                .collect(Collectors.toList());

        List<Booking> findAllByPageable = bookingRepository.findAllByIdInOrderByIdDesc(bookingsIds);

        return findAllByPageable;
    }

    private List<BookingDto> getAllBookingsByUserIdWithoutPageable(Pageable page, Long userId, String state) {
        List<Booking> bookings;

        switch (BookingState.getBookingState(state)) {
            case ALL:
                bookings = getAllBookingsByUserId(userId, page);
                break;
            case FUTURE:
                bookings = bookingRepository.getAllByUserIdAndStartIsAfterNow(userId);
                break;
            case PAST:
                bookings = bookingRepository.getAllBookingsByUserIdPast(userId);
                break;
            case CURRENT:
                bookings = bookingRepository.getAllBookingsByUserIdCurrent(userId);
                break;
            case WAITING:
                bookings = bookingRepository.getAllBookingsByUserIdAndStatus(userId, BookingState.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.getAllBookingsByUserIdAndStatus(userId, BookingState.REJECTED);
                break;
            default:
                throw new NotAvailableException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream()
                .map(BookingMapper::getBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getAllBookingsByOwnerId(Long ownerId, String state, Integer from, Integer size) {
        userExistsById(ownerId);

        if (from == null && size == null) {
            return getAllBookingsByOwnerIdWithoutPageable(null, ownerId, state);
        } else {
            Sort sortById = Sort.by(Sort.Direction.DESC, "id");

            Pageable page = PageRequest.of(from > 0 ? from / size : 0, size, sortById);
            return getAllBookingsByOwnerId(ownerId, page)
                    .stream()
                    .map(BookingMapper::getBookingDto)
                    .collect(Collectors.toList());
        }
    }

    private List<Booking> getAllBookingsByOwnerId(Long ownerId, Pageable page) {
        List<Long> bookingsIds = bookingRepository.findBookingByOwnerId(ownerId, page)
                .getContent()
                .stream()
                .map(t -> t.getId())
                .collect(Collectors.toList());

        List<Booking> findAllByPageable = bookingRepository.findAllByIdInOrderByIdDesc(bookingsIds);

        return findAllByPageable;
    }

    private List<BookingDto> getAllBookingsByOwnerIdWithoutPageable(Pageable page, Long ownerId, String state) {

        List<Booking> bookings;

        userExistsById(ownerId);

        switch (BookingState.getBookingState(state)) {
            case ALL:
                bookings = bookingRepository.getAllBookingByOwnerId(ownerId);
                break;
            case FUTURE:
                bookings = bookingRepository.getAllBookingByOwnerIdFuture(ownerId);
                break;
            case PAST:
                bookings = bookingRepository.getAllBookingByOwnerIdPast(ownerId);
                break;
            case CURRENT:
                bookings = bookingRepository.getAllBookingByOwnerIdCurrent(ownerId);
                break;
            case WAITING:
                bookings = bookingRepository.getAllBookingByOwnerIdStatus(ownerId, BookingState.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.getAllBookingByOwnerIdStatus(ownerId, BookingState.REJECTED);
                break;
            default:
                throw new NotAvailableException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream()
                .map(BookingMapper::getBookingDto)
                .collect(Collectors.toList());
    }

    private void existsById(BookingDto bookingDto) {
        if (!itemService.existsByItemId(bookingDto.getItem().getId())) {
            throw new EntityNotFoundException("Вещь " + bookingDto.getItem().getName() + " не найдена.");
        }
        if (!userService.isUserExists(bookingDto.getBooker().getId())) {
            throw new EntityNotFoundException("Проверьте ID = " + bookingDto.getBooker().getId() + " пользователя");
        }
    }

    private void userExistsById(Long userId) {
        if (!userService.isUserExists(userId)) {
            throw new EntityNotFoundException("Проверьте ID = " + userId + " пользователя");
        }
    }

    private void isAvailable(Item item) {
        if (!item.getAvailable()) {
            throw new NotAvailableException("Вещь " + item.getName() + " недоступна для бронирования");
        }
    }
}
