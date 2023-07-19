package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingAddDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
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
    public BookingDto updateStatus(@RequestHeader(value = USER_ID) Long bookerId,
                                   @PathVariable("bookingId") Long bookingId,
                                   @RequestParam(value = "approved") Boolean approved) {
        log.debug("Получен запрос на изменение статуса бронирования с ID = {} на статус {}", bookingId, approved);
        return bookingServiceDB.updateStatus(bookerId, bookingId, approved);
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
                                                     @RequestParam(value = "state",
                                                             defaultValue = "ALL") String state,
                                                     @RequestParam(value = "from", required = false) @PositiveOrZero Integer from,
                                                     @RequestParam(value = "size", required = false) Integer size) {
        log.debug("Запрошены все бронирования пользователя ID = {}", bookerId);
        return bookingServiceDB.getAllBookingsByUserId(bookerId, state, from, size);
    }

    @GetMapping(value = "/owner")
    public List<BookingDto> getAllBookingsByOwnerId(@RequestHeader(value = USER_ID) Long ownerId,
                                                    @RequestParam(value = "from", required = false) @PositiveOrZero Integer from,
                                                    @RequestParam(value = "size", required = false) Integer size,
                                                    @RequestParam(value = "state",
                                                            defaultValue = "ALL") String state) {
        log.debug("Запрошены все бронирования пользователя ID = {}", ownerId);
        return bookingServiceDB.getAllBookingsByOwnerId(ownerId, state, from, size);
    }
}
