package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.BaseClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addBooking(Long userId, BookingAddDto bookingAddDto) {
        return post("", userId, bookingAddDto);
    }

    public ResponseEntity<Object> updateStatus(Long bookerId, Long bookingId, Boolean approved) {
        Map<String, Object> param = new HashMap<>(
                Map.of("status", String.valueOf(approved), "bookingId", String.valueOf(bookingId)));
        return customPatch("/{bookingId}?approved={status}", bookerId, param);
    }

    protected <T> ResponseEntity<Object> patch(String path, T body) {
        return patch(path, null, null, body);
    }


    public ResponseEntity<Object> getBookingsByUserId(long userId, String state, Integer from, Integer size) {
        if (from != null && size != null) {
            Map<String, Object> parameters = Map.of(
                    "state", state,
                    "from", from,
                    "size", size
            );
            return get("?state={state}&from={from}&size={size}", userId, parameters);
        } else if (!state.equals("ALL")) {
            Map<String, Object> parameters = Map.of("state", state);
            return get("?state={state}", userId, parameters);
        } else {
            return get("", userId);
        }
    }

    public ResponseEntity<Object> getBookingsByOwnerId(long userId, String state, Integer from, Integer size) {
        if (from != null && size != null) {
            Map<String, Object> parameters = Map.of(
                    "state", state,
                    "from", from,
                    "size", size
            );
            return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
        } else if (!state.equals("ALL")) {
            Map<String, Object> parameters = Map.of("state", state);
            return get("/owner?state={state}", userId, parameters);
        } else {
            return get("/owner", userId);
        }
    }

    public ResponseEntity<Object> getBooking(Long bookingId, long bookerId) {
        return get("/" + bookingId, bookerId);
    }
}