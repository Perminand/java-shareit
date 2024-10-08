package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.item.dto.item.ItemDtoFull;
import ru.practicum.shareit.user.markers.Update;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    public static final String USER_HEADER = "X-Sharer-User-Id";
    private final ItemClient itemClient;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAll(@RequestHeader(USER_HEADER) Long userId) {
        log.info("Get userId = {}", userId);
        return itemClient.getItemsByUserId(userId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getById(@RequestHeader(USER_HEADER) Long userId,
                                          @PathVariable("itemId") long itemId) {
        log.info("Get userId = {}, itemId = {}", userId, itemId);
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> search(@RequestHeader(USER_HEADER) @Min(0) Long userId,
                                         @RequestParam(name = "text") @NotNull String text) {
        log.info("Get userId = {}, text = {}", userId, text);
        return itemClient.search(userId, text);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated
    public ResponseEntity<Object> create(@Valid @RequestHeader(USER_HEADER) Long userId,
                                         @Valid @RequestBody ItemDtoFull itemDto) {
        log.info("Post userId = {}, item = {}", userId, itemDto);
        return itemClient.create(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createComment(@RequestHeader(USER_HEADER) Long userId,
                                                @PathVariable Long itemId,
                                                @Valid @RequestBody CommentDto comment) {
        log.info("Post userId = {}, itemId = {}, comment = {}", userId, itemId, comment);
        return itemClient.createComment(userId, itemId, comment);
    }


    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    @Validated({Update.class})
    public ResponseEntity<Object> update(
            @RequestHeader(USER_HEADER) @Min(0) Long userId,
            @PathVariable("itemId") @Min(0) long itemId,
            @Valid @RequestBody ItemDtoFull itemDtoFull) {
        log.info("Patch userId = {}, itemId = {}, item = {}", userId, itemId, itemDtoFull);
        return itemClient.update(userId, itemId, itemDtoFull);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> delete(@RequestHeader(USER_HEADER) Long userId,
                                         @PathVariable("itemId") long itemId) {
        log.info("Delete userId = {}, itemId = {}", userId, itemId);
        return itemClient.deleteItem(userId, itemId);
    }

}
