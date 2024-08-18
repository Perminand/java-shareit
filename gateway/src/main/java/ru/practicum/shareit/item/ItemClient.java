package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.item.ItemDtoFull;

import java.util.Collections;
import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getItemsByUserId(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getItemById(long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> search(Long userId, String text) {
        if (text.isBlank()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return get("/search?text={text}", userId, Map.of("text", text));
    }

    public ResponseEntity<Object> create(Long userId, ItemDtoFull itemDto) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> createComment(Long userId, Long itemId, CommentDto comment) {

        return post("/" + itemId + "/comment", userId, comment);
    }

    public ResponseEntity<Object> update(Long userId, long itemId, ItemDtoFull itemDtoFull) {
        return patch("/" + itemId, userId, itemDtoFull);
    }

    public ResponseEntity<Object> deleteItem(Long userId, long itemId) {
        return delete("/" + itemId, userId);
    }
}
