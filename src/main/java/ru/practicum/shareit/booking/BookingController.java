package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingAddDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private BookingService bookingServiceDB;

    private final String USER_ID = "X-Sharer-User-Id";


    @PostMapping
    public BookingDto addBooking(@Valid @RequestBody BookingAddDto bookingAddDto,
                                 @RequestHeader(value = USER_ID) Long bookerId) {
        log.debug(bookingAddDto + " - получен запрос на добавление вещи");
        bookingAddDto.setUserId(bookerId);
        return bookingServiceDB.addBooking(bookingAddDto);
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingDto updateStatus(@Valid
                                   @RequestHeader(value = USER_ID) Long bookerId,
                                   @PathVariable("bookingId") Long bookingId,
                                   @RequestParam(value = "approved") Boolean APPROVED) {
        log.debug("Получен запрос на изменение статуса бронирования с ID = {} на статус {}", bookingId, APPROVED);
        return bookingServiceDB.updateStatus(bookerId, bookingId, APPROVED);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingDto getBookingByIdByUserId(
            @PathVariable("bookingId") Long bookingId,
            @RequestHeader(value = USER_ID) Long userId) {
        log.debug("Бронирование с ID = {} было запрошена", bookingId);
        return bookingServiceDB.getBookingById(bookingId, userId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsByBookerId(@RequestHeader(value = USER_ID) Long bookerId,
                                                     @RequestParam(value = "state", required = false,
                                                             defaultValue = "ALL") String state) {
        log.debug("Запрошены все бронирования пользователя ID = {}", bookerId);
        return bookingServiceDB.getAllBookingsByUserId(bookerId, state);
    }

    @GetMapping(value = "/owner")
    public List<BookingDto> getAllBookingsByOwnerId(@RequestHeader(value = USER_ID) Long ownerId,
                                                    @RequestParam(value = "state", required = false,
                                                            defaultValue = "ALL") String state) {
        log.debug("Запрошены все бронирования пользователя ID = {}", ownerId);
        return bookingServiceDB.getAllBookingsByOwnerId(ownerId, state);
    }
}
