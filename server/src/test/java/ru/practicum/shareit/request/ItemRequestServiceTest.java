package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exception.error.EntityNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.mappers.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoLite;
import ru.practicum.shareit.request.model.dto.ItemRequestDtoOut;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {

    private final User user = User.builder()
            .id(1L)
            .name("username")
            .email("email@email.com")
            .build();
    private final UserDto userDto = UserDto.builder()
            .id(1L)
            .name("username")
            .email("email@email.com")
            .build();
    private final Item item = Item.builder()
            .id(1L)
            .name("item name")
            .description("description")
            .available(true)
            .owner(user)
            .request(new ItemRequest())
            .build();
    private final ItemRequest request = ItemRequest.builder()
            .id(1L)
            .description("request description")
            .items(List.of(item))
            .build();
    @InjectMocks
    private ItemRequestServiceImpl requestService;
    @Mock
    private Item mockItem;
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    void addNewRequest() {
        ItemRequestDto requestDto = ItemRequestMapper.toItemRequestDto(request);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(requestRepository.save(any(ItemRequest.class))).thenReturn(request);

        ItemRequestDtoOut actualRequestDto = requestService.create(user.getId(), requestDto);

        assertEquals(1L, actualRequestDto.getId());
    }

    @Test
    void getUserRequests() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(requestRepository.findByRequesterId(anyLong(), any(Sort.class))).thenReturn(List.of(request));

        List<ItemRequestDtoLite> actualRequestsDto = requestService.findByUserId(user.getId());

        assertEquals(1, actualRequestsDto.size());
    }

    @Test
    void getAllRequests() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        List<ItemRequestDtoLite> actualRequestsDto = requestService.findByUserId(userDto.getId());
        assertEquals(0, actualRequestsDto.size());
    }

    @Test
    void getRequestById() {
        ItemRequestDto expectedRequestDto = ItemRequestMapper.toItemRequestDto(request);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(requestRepository.findById(request.getId())).thenReturn(Optional.of(request));

        ItemRequestDtoOut actualRequestDto = requestService.getItemRequest(userDto.getId(), request.getId());

        assertEquals(1L, actualRequestDto.getId());
    }

    @Test
    void getRequestByIdWhenRequestIdIsNotValidShouldThrowObjectNotFoundException() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(requestRepository.findById(request.getId())).thenReturn(Optional.empty());

        EntityNotFoundException requestNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> requestService.getItemRequest(userDto.getId(), request.getId()));

        assertEquals(requestNotFoundException.getMessage(), String.format("Нет request с заданным id: 1", request.getId()));
    }
}