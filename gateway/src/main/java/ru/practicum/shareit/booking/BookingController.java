package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.dto.BookingAddDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingClient bookingClient;

    private final String userFromHeader = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addBooking(@Valid @RequestBody BookingAddDto body,
                                             @Positive @RequestHeader(value = userFromHeader) Long bookerId) {
        return bookingClient.addBooking(bookerId, body);
    }

    @PatchMapping(value = "/{bookingId}")
    public ResponseEntity<Object> updateStatus(@RequestHeader(value = userFromHeader) Long bookerId,
                                   @PathVariable("bookingId") Long bookingId,
                                   @RequestParam(value = "approved") Boolean approved) {
        return bookingClient.updateStatus(bookerId, bookingId, approved); // (Long bookerId, Long bookingId, Boolean approved)
    }

    @GetMapping(value = "/{bookingId}")
    public ResponseEntity<Object> getBookingByIdByUserId(
            @PathVariable("bookingId") Long bookingId,
            @RequestHeader(value = userFromHeader) Long userId) {
        return bookingClient.getBooking(bookingId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookingsByBookerId(@RequestHeader(value = userFromHeader) Long bookerId,
                                                     @RequestParam(value = "state",
                                                             defaultValue = "ALL") String state,
                                                     @RequestParam(value = "from", required = false) @PositiveOrZero Integer from,
                                                     @RequestParam(value = "size", required = false) Integer size) {
        return bookingClient.getBookingsByUserId(bookerId, state, from, size);
    }

    @GetMapping(value = "/owner")
    public ResponseEntity<Object> getAllBookingsByOwnerId(@RequestHeader(value = userFromHeader) Long ownerId,
                                                    @RequestParam(value = "from", required = false) @PositiveOrZero Integer from,
                                                    @RequestParam(value = "size", required = false) Integer size,
                                                    @RequestParam(value = "state",
                                                            defaultValue = "ALL") String state) {
        return bookingClient.getBookingsByOwnerId(ownerId, state, from, size);
    }
}
