package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.TestData.*;
import static ru.practicum.shareit.util.JsonUtil.COMMENT_DATE_TIME_FORMATTER;


@WebMvcTest(ItemController.class)
public class ItemControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ItemService itemService;

    @SneakyThrows
    @Test
    void shouldSaveItem() {
        Mockito.when(itemService.create(Mockito.anyLong(), Mockito.any())).thenReturn(itemFullDto);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(itemFullDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemFullDto.getName())))
                .andExpect(jsonPath("$.description", is(itemFullDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemFullDto.getAvailable())))
                .andExpect(jsonPath("$.ownerId", is(itemFullDto.getOwnerId()), Long.class))
                .andExpect(jsonPath("$.requestId", is(itemFullDto.getRequestId()), Long.class));
    }

    @SneakyThrows
    @Test
    void shouldUpdateItem() {
        Mockito.when(itemService.update(Mockito.anyLong(), Mockito.anyLong(), Mockito.any())).thenReturn(itemFullDto);

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(itemFullDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemFullDto.getName())))
                .andExpect(jsonPath("$.description", is(itemFullDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemFullDto.getAvailable())))
                .andExpect(jsonPath("$.ownerId", is(itemFullDto.getOwnerId()), Long.class))
                .andExpect(jsonPath("$.requestId", is(itemFullDto.getRequestId()), Long.class));
    }

    @SneakyThrows
    @Test
    void shouldFindById() {
        Mockito.when(itemService.get(Mockito.anyLong(), Mockito.anyLong())).thenReturn(itemFullDto);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(itemFullDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemFullDto.getName())))
                .andExpect(jsonPath("$.description", is(itemFullDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemFullDto.getAvailable())))
                .andExpect(jsonPath("$.ownerId", is(itemFullDto.getOwnerId()), Long.class))
                .andExpect(jsonPath("$.requestId", is(itemFullDto.getRequestId()), Long.class));
    }

    @SneakyThrows
    @Test
    void shouldFindByUser() {
        Mockito.when(itemService.getAllByUser(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(itemFullDto));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(itemFullDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(itemFullDto.getName())))
                .andExpect(jsonPath("$.[0].description", is(itemFullDto.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(itemFullDto.getAvailable())))
                .andExpect(jsonPath("$.[0].ownerId", is(itemFullDto.getOwnerId()), Long.class))
                .andExpect(jsonPath("$.[0].requestId", is(itemFullDto.getRequestId()), Long.class));
    }

    @SneakyThrows
    @Test
    void shouldFindByText() {
        Mockito.when(itemService.getByText(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(itemFullDto));

        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", "test")
                        .param("from", "0")
                        .param("size", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(itemFullDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(itemFullDto.getName())))
                .andExpect(jsonPath("$.[0].description", is(itemFullDto.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(itemFullDto.getAvailable())))
                .andExpect(jsonPath("$.[0].ownerId", is(itemFullDto.getOwnerId()), Long.class))
                .andExpect(jsonPath("$.[0].requestId", is(itemFullDto.getRequestId()), Long.class));
    }

    @SneakyThrows
    @Test
    void shouldDeleteById() {
        mvc.perform(delete("/items/1").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void shouldAddComment() {
        Mockito.when(itemService.addComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenReturn(commentFullDto);

        CommentCreationDto creationDto = new CommentCreationDto(null, "text");

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(creationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(commentFullDto.getId()), Long.class))
                .andExpect(jsonPath("$.authorName", is(commentFullDto.getAuthorName())))
                .andExpect(jsonPath("$.text", is(commentFullDto.getText())))
                .andExpect(jsonPath("$.created", is(commentFullDto.getCreated()
                        .format(COMMENT_DATE_TIME_FORMATTER))));
    }
}
