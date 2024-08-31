package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.dto.comment.CommentDto;
import ru.practicum.shareit.item.model.dto.comment.CommentDtoOut;
import ru.practicum.shareit.item.model.dto.item.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.booking.controller.BookingController.USER_HEADER;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    private final User user = User.builder()
            .id(1L)
            .name("username")
            .email("my@email.com")
            .build();
    private final Item item = Item.builder()
            .id(1L)
            .name("item name")
            .description("description")
            .owner(user)
            .build();
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService itemService;

    @Test
    @SneakyThrows
    void createItemWhenItemIsValid() {
        Long userId = 0L;
        ItemDto itemDtoToCreate = ItemDto.builder()
                .description("some item description")
                .name("some item name")
                .available(true)
                .build();

        when(itemService.create(userId, itemDtoToCreate)).thenReturn(ItemMapper.toItemDto(ItemMapper.toItem(itemDtoToCreate)));

        String result = mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .header(USER_HEADER, userId)
                        .content(objectMapper.writeValueAsString(itemDtoToCreate)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ItemDto resultItemDto = objectMapper.readValue(result, ItemDto.class);
        assertEquals(itemDtoToCreate.getDescription(), resultItemDto.getDescription());
        assertEquals(itemDtoToCreate.getName(), resultItemDto.getName());
    }


    @Test
    @SneakyThrows
    void getShouldReturnStatusOk() {
        Long itemId = 0L;
        Long userId = 0L;
        ItemDto itemDtoToCreate = ItemDto.builder()
                .id(itemId)
                .description("")
                .name("")
                .available(true)
                .build();

        when(itemService.getItemById(userId, itemId)).thenReturn(itemDtoToCreate);

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/items/{itemId}", itemId)
                        .contentType("application/json")
                        .header(USER_HEADER, userId)
                        .content(objectMapper.writeValueAsString(itemDtoToCreate)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDtoToCreate), result);
    }

    @Test
    @SneakyThrows
    void getAllShouldReturnStatusOk() {
        Long userId = 0L;
        Integer from = 0;
        Integer size = 10;
        List<ItemDto> itemsDtoToExpect = List.of(ItemDto.builder()
                .name("some item name")
                .description("some item description")
                .available(true)
                .build());

        when(itemService.getItemsByUserId(userId)).thenReturn(itemsDtoToExpect);

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/items", from, size)
                        .header(USER_HEADER, userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemsDtoToExpect), result);
    }

    @Test
    @SneakyThrows
    void searchItemsShouldReturnStatusOk() {
        Long userId = 0L;
        Integer from = 0;
        Integer size = 10;
        String text = "find";
        List<ItemDto> itemsDtoToExpect = List.of(ItemDto.builder()
                .name("some item name")
                .description("some item description")
                .available(true)
                .build());

        when(itemService.search(userId, text)).thenReturn(itemsDtoToExpect);

        String result = mockMvc.perform(MockMvcRequestBuilders.get("/items/search", from, size)
                        .header(USER_HEADER, userId)
                        .param("text", text))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemsDtoToExpect), result);
    }


    @Test
    @SneakyThrows
    void createCommentWhenCommentIsValidShouldReturnStatusIsOk() {
        ItemDto itemDtoOut = itemService.create(user.getId(), ItemMapper.toItemDto(item));
        CommentDto commentToAdd = CommentDto.builder()
                .text("some comment")
                .build();
        CommentDtoOut commentDtoOut = CommentDtoOut.builder()
                .id(1L)
                .itemId(item.getId())
                .text(commentToAdd.getText())
                .build();
        when(itemService.createComment(user.getId(), item.getId(), commentToAdd)).thenReturn(commentDtoOut);

        String result = mockMvc.perform(post("/items/{itemId}/comment", item.getId())
                        .contentType("application/json")
                        .header(USER_HEADER, user.getId())
                        .content(objectMapper.writeValueAsString(commentToAdd)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(commentDtoOut), result);
    }
}