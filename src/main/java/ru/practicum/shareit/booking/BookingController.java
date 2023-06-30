package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingAddDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    BookingServiceDB bookingServiceDB;

    @Autowired
    public BookingController(BookingServiceDB bookingServiceDB) {
        this.bookingServiceDB = bookingServiceDB;
    }

    @PostMapping
    public BookingDto addBooking(@Valid @RequestBody BookingAddDto bookingAddDto,
                                 @RequestHeader(value = "X-Sharer-User-Id") Long bookerId) {
        log.error(bookingAddDto + " - получен запрос на добавление вещи");
        bookingAddDto.setUserId(bookerId);
        log.error(bookingAddDto + " - вещь была добавлена");
        return bookingServiceDB.addBooking(bookingAddDto);
    }

    @PatchMapping(value = "/{bookingId}")
    public BookingDto updateStatus(@Valid
                                   @RequestHeader(value = "X-Sharer-User-Id") Long bookerId,
                                   @PathVariable("bookingId") Long bookingId,
                                   @RequestParam(value = "approved") String text) {
        log.error("Бронирование с ID = {} изменено на статус {}", bookingId, text);
        return bookingServiceDB.updateStatus(bookerId, bookingId, text);
    }

    @GetMapping(value = "/{bookingId}")
    public BookingDto getBookingByIdByUserId(
            @PathVariable("bookingId") Long bookingId,
            @RequestHeader(value = "X-Sharer-User-Id") Long userId) {
        log.debug("Бронирование с ID = {} было запрошена", bookingId);
        return bookingServiceDB.getBookingById(bookingId, userId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllBookingsByBookerId(@RequestHeader(value = "X-Sharer-User-Id") Long bookerId,
                                                     @RequestParam(value = "state", required = false,
                                                             defaultValue = "ALL") String state) {
        log.debug("Запрошены все бронирования пользователя ID = {}", bookerId);
        return bookingServiceDB.getAllBookingsByUserId(bookerId, state);
    }

    @GetMapping(value = "/owner")
    public List<BookingDto> getAllBookingsByOwnerId(@RequestHeader(value = "X-Sharer-User-Id") Long ownerId,
                                                    @RequestParam(value = "state", required = false,
                                                            defaultValue = "ALL") String state) {
        log.debug("Запрошены все бронирования пользователя ID = {}", ownerId);
        return bookingServiceDB.getAllBookingsByOwnerId(ownerId, state);
    }
}
