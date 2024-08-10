package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final ItemService itemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Пришел GET запрос на метод getAll");
        return itemService.getItemsByUserId(userId);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("itemId") long itemId) {
        log.info("Пришел GET запрос на метод getById");
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> search(@RequestHeader("X-Sharer-User-Id") Long userId,
                                @RequestParam(name = "text") String text) {
        log.info("Пришел GET запрос на метод search");
        return itemService.search(userId, text);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDtoLite create(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody ItemDto itemDto) {
        log.info("Пришел Post запрос на метод create");
        return itemService.create(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable Long itemId,
                                    @RequestBody CommentDto comment) {
        log.info("Пришел Post запрос на метод createComment");
        return itemService.createComment(userId, itemId, comment);
    }


    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDtoLite update(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable("itemId") long itemId,
            @RequestBody ItemDto itemDto) {
        log.info("Пришел Patch запрос на метод update");
        return itemService.update(userId, itemId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable("itemId") long itemId) {
        log.info("Пришел Delete запрос на метод delete");
        itemService.deleteItem(userId, itemId);
    }

}
