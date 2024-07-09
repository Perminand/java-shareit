package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllUsers() {
        log.info(" ==> GET /users ");
        List<UserDto> users = userService.getAllUsers();
        log.info(" <== {}", users);
        return users;
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getById(@PathVariable long userId) {
        log.info(" ==> GET /users/{}", userId);
        UserDto user = userService.getById(userId);
        log.info(" <== {}", user);
        return user;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        log.info(" ==> POST /users/ {}", user);
        User newUser = userService.create(user);
        log.info(" <== {}", newUser);
        return newUser;
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User update(@PathVariable long userId, @RequestBody User user) {
        log.info(" ==> PATCH /users/ {}", user);
        User newUser = userService.update(userId, user);
        log.info(" <== {}", newUser);
        return newUser;
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable long userId) {
        log.info(" ==> DELETE /users/{}", userId);
        userService.deleteById(userId);
        log.info(" <== OK");
    }
}
