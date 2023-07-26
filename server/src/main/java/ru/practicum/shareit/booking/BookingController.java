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

    private BookingService bookingService;

    private final String userFromHeader = "X-Sharer-User-Id";

    @PostMapping
    public BookingDto addBooking(@Valid @RequestBody BookingAddDto bookingAddDto,
                                 @RequestHeader(value = userFromHeader) Long bookerId) {
        log.debug(bookingAddDto + " - получен запрос на добавление вещи");
        bookingAddDto.setUserId(bookerId);
        return bookingService.addBooking(bookingAddDto);
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingDto updateStatus(@RequestHeader(value = userFromHeader) Long bookerId,
                                   @PathVariable("bookingId") Long bookingId,
                                   @RequestParam(value = "approved") Boolean approved) {
        log.debug("Получен запрос на изменение статуса бронирования с ID = {} на статус {}", bookingId, approved);
        return bookingService.updateStatus(bookerId, bookingId, approved);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingDto getBookingByIdByUserId(
            @PathVariable("bookingId") Long bookingId,
            @RequestHeader(value = userFromHeader) Long userId) {
        log.debug("Бронирование с ID = {} было запрошена", bookingId);
        return bookingService.getBookingById(bookingId, userId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsByBookerId(@RequestHeader(value = userFromHeader) Long bookerId,
                                                     @RequestParam(value = "state",
                                                             defaultValue = "ALL") String state,
                                                     @RequestParam(value = "from", required = false) @PositiveOrZero Integer from,
                                                     @RequestParam(value = "size", required = false) Integer size) {
        log.error("ЗАПРОС STATE={} ДЛЯ ПОЛЬЗОВАТЕЛЯ {}", state, bookerId);
        log.debug("Запрошены все бронирования пользователя ID = {}", bookerId);
        return bookingService.getAllBookingsByUserId(bookerId, state, from, size);
    }

    @GetMapping(value = "/owner")
    public List<BookingDto> getAllBookingsByOwnerId(@RequestHeader(value = userFromHeader) Long ownerId,
                                                    @RequestParam(value = "from", required = false) @PositiveOrZero Integer from,
                                                    @RequestParam(value = "size", required = false) Integer size,
                                                    @RequestParam(value = "state",
                                                            defaultValue = "ALL") String state) {
        log.debug("Запрошены все бронирования пользователя ID = {}", ownerId);
        return bookingService.getAllBookingsByOwnerId(ownerId, state, from, size);
    }
}
