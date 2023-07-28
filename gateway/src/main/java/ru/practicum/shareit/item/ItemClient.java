package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.BaseClient;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addItem(long userId, ItemDto itemDto) {
        return post("", userId,  itemDto);
    }

    public ResponseEntity<Object> updateItem(ItemUpdateDto itemUpdateDto, long userId, long itemId) {
        return patch("/" + itemId, userId, itemUpdateDto);
    }

    public ResponseEntity<Object> findByIdAndOwner(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> findAllByUser(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getItemByParam(String text) {
        return get("/search?text=" + text);
    }

    public ResponseEntity<Object> addComment(long itemId, long userId, CommentAddDto commentAddDto) {
        Map<String, Object> parameters = Map.of("itemId", itemId);
        return post("/{itemId}/comment", userId, parameters, commentAddDto);
    }
}
