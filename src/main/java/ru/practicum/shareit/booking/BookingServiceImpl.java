package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingServiceImpl implements BookingService {

    final BookingRepository bookingRepository;

    final ItemService itemService;

    final UserRepository userRepository;

    private final Boolean APPROVE_STATUS = true;

    @Override
    public BookingDto addBooking(BookingAddDto bookingAddDto) {
        User user = userRepository.getReferenceById(bookingAddDto.getUserId());
        ItemDto itemDto = itemService.getItemById (bookingAddDto.getItemId());
        BookingDto bookingDto = BookingAddMapper.getBookingDto(bookingAddDto, itemDto, user);
        if (bookingDto.getItem().getOwner().equals(bookingAddDto.getUserId())) {
            throw new EntityNotFoundException("Владелец вещи не может создавать бронирование");
        }
        bookingDto.setStatus(BookingState.WAITING);
        isAvailable(ItemMapper.getItem(bookingDto.getItem()));

        if (bookingDto.getStart().isAfter(bookingDto.getEnd()) || bookingDto.getStart().equals(bookingDto.getEnd())
                || bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new NotAvailableException("Проверьте даты бронирования");
        }
        existsById(bookingDto);

        bookingAddDto.setStatus(BookingState.WAITING);
        return BookingMapper.getBookingDto(bookingRepository.save(BookingMapper.getBooking(bookingDto)));
    }

    @Override
    public BookingDto updateStatus(Long bookerId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.getReferenceById(bookingId);
        if (!(booking.getItem().getOwner().equals(bookerId))) {
            throw new EntityNotFoundException("Проверьте ID пользователя");
        }
        if (APPROVE_STATUS.equals(approved)) {
            if (!booking.getStatus().equals(BookingState.WAITING)) {
                throw new NotAvailableException("Бронирование уже подтверждено");
            }
            booking.setStatus(BookingState.APPROVED);
        } else {
            booking.setStatus(BookingState.REJECTED);
        }
        return BookingMapper.getBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long bookerId, Long ownerId) {
        Booking booking = bookingRepository.getReferenceById(bookingId);
        if (booking.getItem().getOwner().equals(ownerId) || booking.getUser().getId().equals(bookerId)) {
            return BookingMapper.getBookingDto(booking);
        } else {
            throw new EntityNotFoundException("Проверьте ID пользователя");
        }
    }

    @Override
    public List<BookingDto> getAllBookingsByUserId(Long userId, String state) {
        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookings;

        if (bookingRepository.findBookingByUserIdEqualsOrderByIdDesc(userId).size() == 0) {
            throw new EntityNotFoundException("Проверьте ID пользователя");
        }

        switch (ConverterStatus.getBookingState(state)) {
            case ALL :
                bookings = bookingRepository.findBookingByUserIdEqualsOrderByIdDesc(userId);
                break;
            case FUTURE:
                bookings = bookingRepository.findBookingByUserIdEqualsAndStartAfterOrderByIdDesc(userId, now);
                break;
            case PAST:
                bookings = bookingRepository.findBookingByUserIdEqualsAndEndBeforeOrderByIdDesc(userId, now);
                break;
            case CURRENT:
                bookings = bookingRepository.findBookingByUserIdEqualsAndStartBeforeAndEndAfter(
                        userId, now, now);
                break;
            case WAITING:
                bookings = bookingRepository.findBookingByUserIdEqualsAndStatusEqualsOrderByIdDesc(
                        userId, BookingState.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findBookingByUserIdEqualsAndStatusEqualsOrderByIdDesc(
                        userId, BookingState.REJECTED);
                break;
            default:
                throw new NotAvailableException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream()
                .map(BookingMapper::getBookingDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getAllBookingsByOwnerId(Long ownerId, String state) {
        LocalDateTime now = LocalDateTime.now();

        List<Booking> bookings;

        if (bookingRepository.findBookingByOwnerIdOrderByIdDesc(ownerId).size() == 0) {
            throw new EntityNotFoundException("Проверьте ID пользователя");
        }
        switch (ConverterStatus.getBookingState(state)) {
            case ALL:
                bookings = bookingRepository.findBookingByOwnerIdOrderByIdDesc(ownerId);
                break;
            case FUTURE:
                bookings = bookingRepository.findBookingByOwnerIdAndStartAfterOrderByIdDesc(ownerId, now);
                break;
            case PAST:
                bookings = bookingRepository.findBookingByOwnerIdAndEndBeforeOrderByIdDesc(ownerId);
                break;
            case CURRENT:
                bookings = bookingRepository.findBookingByOwnerIdAndStartBeforeAndEndAfterOrderByIdDesc(
                        ownerId);
                break;
            case WAITING:
            case REJECTED:
                bookings = bookingRepository.findBookingByOwnerIdAndStatusEqualsOrderByIdDesc(
                        ownerId, state);
                break;
            default:
                throw new NotAvailableException("Unknown state: UNSUPPORTED_STATUS");
        }
        return bookings.stream()
                .map(BookingMapper::getBookingDto).collect(Collectors.toList());
    }

    private void existsById(BookingDto bookingDto) {
        if (!itemService.existsByItemId(bookingDto.getItem().getId())) {
            throw new EntityNotFoundException("Вещь не найдена.");
        }
        if (!userRepository.existsById(bookingDto.getBooker().getId())) {
            throw new EntityNotFoundException("Пользователь не найден.");
        }
    }

    private void isAvailable(Item item) {
        if (!item.getAvailable()) {
            throw new NotAvailableException("Вещь недоступна для бронирования");
        }
    }
}
