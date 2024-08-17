package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoOut;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RequestServiceIT {

    private final UserDto userDto = UserDto.builder()
            .name("name")
            .email("email@email.com")
            .build();
    private final ItemRequestDto requestDto = ItemRequestDto.builder()
            .description("request description")
            .build();
    @Autowired
    private ItemRequestService requestService;
    @Autowired
    private UserService userService;

    @Test
    void addNewRequest() {
        UserDto addedUser = userService.add(userDto);
        requestService.add(addedUser.getId(), requestDto);

        List<ItemRequestDtoOut> actualRequests = requestService.getUserRequests(addedUser.getId());

        assertEquals("request description", actualRequests.get(0).getDescription());
    }

    @Test
    void getRequestByIdWhenRequestIdIsNotValidShouldThrowObjectNotFoundException() {
        Long requestId = 2L;

        Assertions
                .assertThrows(RuntimeException.class,
                        () -> requestService.getRequestById(userDto.getId(), requestId));
    }
}