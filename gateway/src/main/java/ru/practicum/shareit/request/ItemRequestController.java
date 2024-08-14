package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestClient;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final RequestClient requestClient;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemRequestForUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @PathVariable(value = "requestId", required = false) Long requestId) {

        return requestClient.findByUserId(userId);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable(value = "requestId") Long requestId) {
        return requestClient.getItemRequest(userId, requestId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> getItemRequestOwnerUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(name = "from") int from,
                                                        @RequestParam(name = "size") int size) {

        return requestClient.findItemRequestOwnerUser(userId, from, size);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto createItemRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Пришел Post запрос на метод createItemRequest");
        return requestClient.create(userId, itemRequestDto);
    }
}
