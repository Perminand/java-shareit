package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.dto.comment.CommentDto;
import ru.practicum.shareit.item.model.dto.item.ItemDto;
import ru.practicum.shareit.item.model.dto.item.ItemDtoLite;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    public static final String USER_HEADER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getAll(@RequestHeader(USER_HEADER) Long userId) {
        log.info("Get userId = {}", userId);
        return itemService.getItemsByUserId(userId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getById(@RequestHeader(USER_HEADER) Long userId, @PathVariable("itemId") long itemId) {
        log.info("Get userId = {}, itemId = {}", userId, itemId);
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> search(@RequestHeader(USER_HEADER) Long userId,
                                @RequestParam(name = "text") String text) {
        log.info("Get userId = {}, text = {}", userId, text);
        return itemService.search(userId, text);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestHeader(USER_HEADER) Long userId, @RequestBody @Validated ItemDto itemDto) {
        log.info("Post userId = {}, itemDto = {}", userId, itemDto);
        return itemService.create(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@RequestHeader(USER_HEADER) Long userId,
                                    @PathVariable Long itemId,
                                    @RequestBody CommentDto comment) {
        log.info("Post userId = {}, itemId = {}, comment = {}", userId, itemId, comment);
        return itemService.createComment(userId, itemId, comment);
    }


    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDtoLite update(
            @RequestHeader(USER_HEADER) Long userId,
            @PathVariable("itemId") long itemId,
            @RequestBody ItemDto itemDto) {
        log.info("Patch userId = {}, itemId = {}, item = {}", userId, itemId, itemDto);
        return itemService.update(userId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@RequestHeader(USER_HEADER) Long userId, @PathVariable("itemId") long itemId) {
        log.info("Delete userId = {}, itemId = {}", userId, itemId);
        itemService.deleteItem(userId, itemId);
    }

}
