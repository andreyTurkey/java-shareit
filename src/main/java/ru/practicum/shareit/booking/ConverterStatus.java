package ru.practicum.shareit.booking;

import ru.practicum.shareit.exception.NotAvailableException;

public class ConverterStatus {

    public static BookingState getBookingState(String status) {
        switch (status) {
            case ("ALL"):
                return BookingState.ALL;
            case ("FUTURE"):
                return BookingState.FUTURE;
            case ("APPROVED"):
                return BookingState.APPROVED;
            case ("PAST"):
                return BookingState.PAST;
            case ("CURRENT"):
                return BookingState.CURRENT;
            case ("WAITING"):
                return BookingState.WAITING;
            case ("REJECTED"):
                return BookingState.REJECTED;
            default:
                throw new NotAvailableException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}
