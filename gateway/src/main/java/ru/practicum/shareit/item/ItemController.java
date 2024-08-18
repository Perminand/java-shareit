package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.item.ItemDtoFull;
import ru.practicum.shareit.item.dto.item.ItemDtoLite;

import javax.xml.stream.events.Comment;

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
                                         @RequestBody ItemDtoFull itemDto) {
        log.info("Post userId = {}, item = {}", userId, itemDto);
        return itemClient.create(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @PathVariable Long itemId,
                                                @RequestBody String comment) {
        log.info("Post userId = {}, itemId = {}, comment = {}", userId, itemId, comment);
        CommentDto commentDto = new CommentDto(comment);
        return itemClient.createComment(userId, itemId, commentDto);
    }


    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> update(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable("itemId") long itemId,
            @RequestBody ItemDtoFull itemDtoFull) {
        log.info("Patch userId = {}, itemId = {}, item = {}", userId, itemId, itemDtoFull);
        return itemClient.update(userId, itemId, itemDtoFull);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> delete(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable("itemId") long itemId) {
        log.info("Delete userId = {}, itemId = {}", userId, itemId);
        return itemClient.deleteItem(userId, itemId);
    }

}
