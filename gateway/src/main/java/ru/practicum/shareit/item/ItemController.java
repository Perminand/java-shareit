package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.item.ItemDto;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get userId = {}", userId);
        return itemClient.getItemsByUserId(userId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable("itemId") long itemId) {
        log.info("Get userId = {}, itemId = {}", userId, itemId);
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> search(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam(name = "text") String text) {
        log.info("Get userId = {}, text = {}", userId, text);
        return itemClient.search(userId, text);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestBody ItemDto itemDto) {
        log.info("Post userId = {}, item = {}", userId, itemDto);
        return itemClient.create(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable Long itemId,
                                                @RequestBody CommentDto comment) {
        log.info("Post userId = {}, itemId = {}, comment = {}", userId, itemId, comment);
        return itemClient.createComment(userId, itemId, comment);
    }


    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> update(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable("itemId") long itemId,
            @RequestBody ItemDto itemDto) {
        log.info("Patch userId = {}, itemId = {}, item = {}", userId, itemId, itemDto);
        return itemClient.update(userId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> delete(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable("itemId") long itemId) {
        log.info("Delete userId = {}, itemId = {}", userId, itemId);
        return itemClient.deleteItem(userId, itemId);
    }

}
