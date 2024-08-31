package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.markers.Update;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserClient userClient;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getAllUsers() {
        log.info("Get");
        return userClient.getAllUsers();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getById(@PathVariable @Min(0) long userId) {
        log.info("Get userId = {}", userId);
        return userClient.getById(userId);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto user) {
        log.info("Post user = {}", user);
        return userClient.create(user);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @Validated({Update.class})
    public ResponseEntity<Object> update(@Valid @PathVariable @Min(0) long userId,
                                         @RequestBody @Min(0) UserDto userDto) {
        log.info("Patch userId = {}, user = {}", userId, userDto);
        userDto.setId(userId);
        return userClient.update(userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> deleteById(@PathVariable @Min(0) long userId) {
        log.info("Delete userId = {}", userId);
        return userClient.deleteById(userId);
    }
}
