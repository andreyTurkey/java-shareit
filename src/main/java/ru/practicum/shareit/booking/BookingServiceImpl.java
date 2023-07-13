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
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
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

    UserRepository userRepository;

    EntityManager em;

    public List<Booking> getAllBookingsByUserIdAndFuture(Long userId) {
        List<Booking> bookings = em.createQuery(
                        "SELECT b from Booking b WHERE b.user.id =:id AND b.start > now() ORDER BY id DESC")
                .setParameter("id", userId)
                .getResultList();
        return bookings;
    }

    public List<Booking> getAllBookingsByUserIdPast(Long userId) {
        List<Booking> bookings = em.createQuery(
                        "SELECT b from Booking b WHERE b.user.id =:id AND b.end < now() ORDER BY id DESC")
                .setParameter("id", userId)
                .getResultList();
        return bookings;
    }

    public List<Booking> getAllBookingsByUserIdCurrent(Long userId) {
        List<Booking> bookings = em.createQuery(
                        "SELECT b from Booking b WHERE b.user.id =:id AND b.start <= now() AND b.end >= now()")
                .setParameter("id", userId)
                .getResultList();
        return bookings;
    }

    public List<Booking> getAllBookingsByUserIdAndStatus(Long userId, BookingState state) {
        List<Booking> bookings = em.createQuery(
                        "SELECT b from Booking b WHERE b.user.id =:id AND b.status  like :state")
                .setParameter("id", userId)
                .setParameter("state", state)
                .getResultList();
        return bookings;
    }

    @Override
    public BookingDto addBooking(BookingAddDto bookingAddDto) {
        User user = userRepository.getReferenceById(bookingAddDto.getUserId());
        ItemDto itemDto = itemService.getItemById(bookingAddDto.getItemId());
        BookingDto bookingDto = BookingAddMapper.getBookingDto(bookingAddDto, itemDto, user);
        if (bookingDto.getItem().getOwner().equals(bookingAddDto.getUserId())) {
            throw new EntityNotFoundException("Владелец вещи не может создавать бронирование");
        }
        bookingDto.setStatus(BookingState.WAITING);
        isAvailable(ItemMapper.getItem(bookingDto.getItem()));

        List<Booking> allBookingByItem = bookingRepository.findAllByItem_Id(bookingDto.getItem().getId())
                .stream().filter(booking -> {
                    return booking.getStart().isBefore(bookingDto.getStart())
                            && booking.getEnd().isAfter(bookingDto.getStart());
                }).collect(Collectors.toList());

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
        } else if (from < 0 || size <= 0) {
            throw new NotAvailableException("Проверьте параметры запроса");
        } else {
            Sort sortById = Sort.by(Sort.Direction.DESC, "id");
            Pageable page = PageRequest.of(from > 0 ? from / size : 0, size, sortById);
            return getAllBookingsByUserId(userId, page).stream().map(BookingMapper::getBookingDto)
                    .collect(Collectors.toList());
        }
    }

    public List<Booking> getAllBookingsByUserId(Long userId, Pageable page) {
        List<Long> bookingsIds = bookingRepository.findBookingByUserId(userId, page).getContent().stream().map(t -> {
            return t.getId();
        }).collect(Collectors.toList());

        List<Booking> findAllByPageable = bookingRepository.findAllByBookingsId(bookingsIds);

        return findAllByPageable;
    }

    public List<BookingDto> getAllBookingsByUserIdWithoutPageable(Pageable page, Long userId, String state) {
        List<Booking> bookings;

        switch (BookingState.getBookingState(state)) {
            case ALL:
                bookings = getAllBookingsByUserId(userId, page);
                break;
            case FUTURE:
                bookings = getAllBookingsByUserIdAndFuture(userId);
                break;
            case PAST:
                bookings = getAllBookingsByUserIdPast(userId);
                break;
            case CURRENT:
                bookings = getAllBookingsByUserIdCurrent(userId);
                break;
            case WAITING:
                bookings = getAllBookingsByUserIdAndStatus(userId, BookingState.WAITING);
                break;
            case REJECTED:
                bookings = getAllBookingsByUserIdAndStatus(userId, BookingState.REJECTED);
                break;
            default:
                throw new NotAvailableException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream()
                .map(BookingMapper::getBookingDto).collect(Collectors.toList());
    }

    public List<Booking> getAllBookingByOwnerId(Long ownerId) {
        List<Booking> bookings = em.createQuery(
                        "SELECT b from Booking b join Item i on b.item.id = i.id WHERE i.owner =:id ORDER BY b.id DESC")
                .setParameter("id", ownerId)
                .getResultList();
        return bookings;
    }

    public List<Booking> getAllBookingByOwnerIdFuture(Long ownerId) {
        List<Booking> bookings = em.createQuery(
                        "SELECT b from Booking b join Item i on b.item.id = i.id WHERE i.owner =:id " +
                                " AND b.start >= now() ORDER BY b.id DESC")
                .setParameter("id", ownerId)
                .getResultList();
        return bookings;
    }

    public List<Booking> getAllBookingByOwnerIdPast(Long ownerId) {
        List<Booking> bookings = em.createQuery(
                        "SELECT b from Booking b join Item i on b.item.id = i.id WHERE i.owner =:id " +
                                " AND b.end <= now() ORDER BY b.id DESC")
                .setParameter("id", ownerId)
                .getResultList();
        return bookings;
    }

    public List<Booking> getAllBookingByOwnerIdCurrent(Long ownerId) {
        List<Booking> bookings = em.createQuery(
                        "SELECT b from Booking b join Item i on b.item.id = i.id WHERE i.owner =:id " +
                                " AND b.start <= now() AND  b.end >= now() ORDER BY b.id DESC")
                .setParameter("id", ownerId)
                .getResultList();
        return bookings;
    }

    public List<Booking> getAllBookingByOwnerIdStatus(Long ownerId, BookingState state) {
        List<Booking> bookings = em.createQuery(
                        "SELECT b from Booking b join Item i on b.item.id = i.id WHERE i.owner =:id " +
                                " AND b.status  like :state ORDER BY b.id DESC")
                .setParameter("id", ownerId)
                .setParameter("state", state)
                .getResultList();
        return bookings;
    }

    @Override
    public List<BookingDto> getAllBookingsByOwnerId(Long ownerId, String state, Integer from, Integer size) {
        userExistsById(ownerId);

        if (from == null && size == null) {
            return getAllBookingsByOwnerIdWithoutPageable(null, ownerId, state);
        } else if (from < 0 || size <= 0) {
            throw new NotAvailableException("Проверьте параметры запроса");
        } else {
            Sort sortById = Sort.by(Sort.Direction.DESC, "id");

            Pageable page = PageRequest.of(from > 0 ? from / size : 0, size, sortById);
            return getAllBookingsByOwnerId(ownerId, page).stream().map(BookingMapper::getBookingDto)
                    .collect(Collectors.toList());
        }
    }

    public List<Booking> getAllBookingsByOwnerId(Long ownerId, Pageable page) {
        List<Long> bookingsIds = bookingRepository.findBookingByOwnerId(ownerId, page).getContent().stream().map(t -> {
            return t.getId();
        }).collect(Collectors.toList());

        List<Booking> findAllByPageable = bookingRepository.findAllByBookingsId(bookingsIds);

        return findAllByPageable;
    }

    public List<BookingDto> getAllBookingsByOwnerIdWithoutPageable(Pageable page, Long ownerId, String state) {

        List<Booking> bookings;

        userExistsById(ownerId);

        switch (BookingState.getBookingState(state)) {
            case ALL:
                bookings = getAllBookingByOwnerId(ownerId);
                break;
            case FUTURE:
                bookings = getAllBookingByOwnerIdFuture(ownerId);
                break;
            case PAST:
                bookings = getAllBookingByOwnerIdPast(ownerId);
                break;
            case CURRENT:
                bookings = getAllBookingByOwnerIdCurrent(ownerId);
                break;
            case WAITING:
                bookings = getAllBookingByOwnerIdStatus(ownerId, BookingState.WAITING);
                break;
            case REJECTED:
                bookings = getAllBookingByOwnerIdStatus(ownerId, BookingState.REJECTED);
                break;
            default:
                throw new NotAvailableException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream()
                .map(BookingMapper::getBookingDto).collect(Collectors.toList());
    }

    private void existsById(BookingDto bookingDto) {
        if (!itemService.existsByItemId(bookingDto.getItem().getId())) {
            throw new EntityNotFoundException("Вещь " + bookingDto.getItem().getName() + " не найдена.");
        }
        if (!userRepository.existsById(bookingDto.getBooker().getId())) {
            throw new EntityNotFoundException("Проверьте ID = " + bookingDto.getBooker().getId() + " пользователя");
        }
    }

    private void userExistsById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Проверьте ID = " + userId + " пользователя");
        }
    }

    private void isAvailable(Item item) {
        if (!item.getAvailable()) {
            throw new NotAvailableException("Вещь " + item.getName() + " недоступна для бронирования");
        }
    }
}
