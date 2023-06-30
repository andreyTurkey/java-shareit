package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.AvailableException;
import ru.practicum.shareit.exception.EntityNotFoundExceptionCustom;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingServiceDB {

    private final BookingRepository bookingRepository;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final BookingMapper bookingMapper;

    private final BookingAddMapper bookingMapperAdd;

    @Override
    public BookingDto addBooking(BookingAddDto bookingAddDto) {
        BookingDto bookingDto = bookingMapperAdd.getBookingDto(bookingAddDto);

        if (bookingDto.getItem().getOwner() == bookingAddDto.getUserId())
            throw new EntityNotFoundExceptionCustom("Владелец вещи не может создавать бронирование");

        bookingDto.setStatus(BookingState.WAITING);
        isAvailable(bookingDto.getItem());

        if (bookingDto.getStart().isAfter(bookingDto.getEnd()) || bookingDto.getStart().equals(bookingDto.getEnd())
                || bookingDto.getStart().isBefore(LocalDateTime.now()) || bookingDto.getEnd().isBefore(LocalDateTime.now()))
            throw new AvailableException("Проверьте даты бронирования");

        existsById(bookingDto);

        bookingAddDto.setStatus(BookingState.WAITING);
        return bookingMapper.getBookingDto(bookingRepository.save(bookingMapper.getBooking(bookingDto)));
    }

    @Override
    public BookingDto updateStatus(Long bookerId, Long bookingId, String text) {
        Booking booking = bookingRepository.getById(bookingId);
        if (!(booking.getItem().getOwner() == bookerId))
            throw new EntityNotFoundExceptionCustom("Проверьте ID пользователя");
        if (text.equals("true")) {
            if (booking.getStatus().equals(BookingState.APPROVED))
                throw new AvailableException("Бронирование уже подтверждено");
            booking.setStatus(BookingState.APPROVED);
        } else {
            booking.setStatus(BookingState.REJECTED);
        }
        return bookingMapper.getBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long bookingId, Long bookerId, Long ownerId) {
        Booking booking = bookingRepository.getById(bookingId);
        if (booking.getItem().getOwner() == ownerId || booking.getUser().getId() == bookerId) {
            return bookingMapper.getBookingDto(booking);
        } else {
            throw new EntityNotFoundExceptionCustom("Проверьте ID пользователя");
        }
    }

    @Override
    public List<BookingDto> getAllBookingsByUserId(Long userId, String state) {
        LocalDateTime now = LocalDateTime.now();
        if (bookingRepository.findBookingByUserIdEqualsOrderByIdDesc(userId).size() == 0)
            throw new EntityNotFoundExceptionCustom("Проверьте ID пользователя");

        switch (state) {
            case "ALL":
                return bookingRepository.findBookingByUserIdEqualsOrderByIdDesc(userId).stream()
                        .map(bookingMapper::getBookingDto).collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository.findBookingByUserIdEqualsAndStartAfterOrderByIdDesc(userId, now).stream()
                        .map(bookingMapper::getBookingDto).collect(Collectors.toList());
            case "PAST":
                return bookingRepository.findBookingByUserIdEqualsAndEndBeforeOrderByIdDesc(userId, now).stream()
                        .map(bookingMapper::getBookingDto).collect(Collectors.toList());
            case "CURRENT":
                return bookingRepository.findBookingByUserIdEqualsAndStartBeforeAndEndAfter(
                                userId, now, now).stream()
                        .map(bookingMapper::getBookingDto).collect(Collectors.toList());
            case "WAITING":
                return bookingRepository.findBookingByUserIdEqualsAndStatusEqualsOrderByIdDesc(
                                userId, BookingState.WAITING).stream()
                        .map(bookingMapper::getBookingDto).collect(Collectors.toList());
            case "REJECTED":
                return bookingRepository.findBookingByUserIdEqualsAndStatusEqualsOrderByIdDesc(
                                userId, BookingState.REJECTED).stream()
                        .map(bookingMapper::getBookingDto).collect(Collectors.toList());
            default:
                throw new AvailableException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<BookingDto> getAllBookingsByOwnerId(Long ownerId, String state) {
        LocalDateTime now = LocalDateTime.now();

        if (bookingRepository.findBookingByOwnerIdOrderByIdDesc(ownerId).size() == 0)
            throw new EntityNotFoundExceptionCustom("Проверьте ID пользователя");

        switch (state) {
            case "ALL":
                return bookingRepository.findBookingByOwnerIdOrderByIdDesc(ownerId).stream()
                        .map(bookingMapper::getBookingDto).collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository.findBookingByOwnerIdAndStartAfterOrderByIdDesc(ownerId, now).stream()
                        .map(bookingMapper::getBookingDto).collect(Collectors.toList());
            case "PAST":
                return bookingRepository.findBookingByOwnerIdAndEndBeforeOrderByIdDesc(ownerId).stream()
                        .map(bookingMapper::getBookingDto).collect(Collectors.toList());
            case "CURRENT":
                return bookingRepository.findBookingByOwnerIdAndStartBeforeAndEndAfterOrderByIdDesc(
                                ownerId).stream()
                        .map(bookingMapper::getBookingDto).collect(Collectors.toList());
            case "WAITING":
                List<Booking> bookings = bookingRepository.findBookingByOwnerIdAndStatusEqualsOrderByIdDesc(
                        ownerId, state);
                log.error(bookings + "ПРИШЕЛ ЗАПРОС НА WAITING");
                return bookingRepository.findBookingByOwnerIdAndStatusEqualsOrderByIdDesc(
                                ownerId, state).stream()
                        .map(bookingMapper::getBookingDto).collect(Collectors.toList());
            case "REJECTED":
                List<Booking> bookings1 = bookingRepository.findBookingByOwnerIdAndStatusEqualsOrderByIdDesc(
                        ownerId, state);
                log.error(bookings1 + "ПРИШЕЛ ЗАПРОС НА REJECTED");
                return bookingRepository.findBookingByOwnerIdAndStatusEqualsOrderByIdDesc(
                                ownerId, state).stream()
                        .map(bookingMapper::getBookingDto).collect(Collectors.toList());
            default:
                throw new AvailableException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    private void existsById(BookingDto bookingDto) {
        log.error(bookingDto + " - bookingDto");
        if (!itemRepository.existsById(bookingDto.getItem().getId()))
            throw new EntityNotFoundException("Вещь не найдена.");
        if (!userRepository.existsById(bookingDto.getBooker().getId())) throw new EntityNotFoundException(
                "Пользователь не найден.");
    }

    private void isAvailable(Item item) {
        if (!item.getAvailable()) throw new AvailableException("Вещь недоступна для бронирования");
    }
}
