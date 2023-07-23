package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.BaseClient;
import ru.practicum.shareit.item.dto.CommentAddDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

@Service
public class ItemRequestClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addItem(ItemDto itemDto) {
        return post("", itemDto);
    }

    public ResponseEntity<Object> updateItem(ItemUpdateDto itemUpdateDto, long userId, long itemId) {
        return patch("/" + itemId, itemUpdateDto);
    }

    public ResponseEntity<Object> findByIdAndOwner(long itemId, long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> findAllByUser(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getItemByParam(String text) {
        return get("/search?text="+text);
    }

    public ResponseEntity<Object> addComment(CommentAddDto commentAddDto) {
        return post("/{itemId}/comment", commentAddDto.getItemId(), commentAddDto);
    }
}
