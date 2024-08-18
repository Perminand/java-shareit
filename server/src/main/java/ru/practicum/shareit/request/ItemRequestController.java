package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoLite;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoOut;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    public static final String USER_HEADER = "X-Sharer-User-Id";

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDtoLite> getItemRequestForUser(@RequestHeader(USER_HEADER) Long userId,
                                                          @PathVariable(value = "requestId", required = false) Long requestId) {
        log.info("Get userId = {}, requestId = {}", userId, requestId);
        return itemRequestService.findByUserId(userId);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDtoOut getItemRequest(@RequestHeader(USER_HEADER) Long userId,
                                            @PathVariable(value = "requestId") Long requestId) {
        log.info("Get userId = {}, requestId = {}", userId, requestId);
        return itemRequestService.getItemRequest(userId, requestId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> getItemRequestOwnerUser(@RequestHeader(USER_HEADER) Long userId,
                                                        @RequestParam(name = "from") int from,
                                                        @RequestParam(name = "size") int size) {
        log.info("Get userId = {}, from = {}, size = {}", userId, from, size);
        return itemRequestService.findItemRequestOwnerUser(userId, from, size);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDtoOut createItemRequest(@RequestHeader(USER_HEADER) Long userId,
                                               @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Post userId = {}, itemRequest = {}", userId, itemRequestDto);
        return itemRequestService.create(userId, itemRequestDto);
    }
}
