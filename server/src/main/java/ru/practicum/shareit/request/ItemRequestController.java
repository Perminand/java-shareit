package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoLite;
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

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDtoLite> getItemRequestForUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @PathVariable(value = "requestId", required = false) Long requestId) {

        return itemRequestService.findByUserId(userId);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDtoLite getItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                          @PathVariable(value = "requestId") Long requestId) {
        return itemRequestService.getItemRequest(userId, requestId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> getItemRequestOwnerUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(name = "from") int from,
                                                  @RequestParam(name = "size") int size) {

        return itemRequestService.findItemRequestOwnerUser(userId, from, size);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto createItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Пришел Post запрос на метод createItemRequest");
        return itemRequestService.create(userId, itemRequestDto);
    }
}
