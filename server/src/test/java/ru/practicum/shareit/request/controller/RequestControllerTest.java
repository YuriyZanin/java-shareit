package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.TestData.requestDto;
import static ru.practicum.shareit.util.JsonUtil.DEFAULT_DATE_TIME_FORMATTER;

@WebMvcTest(ItemRequestController.class)
public class RequestControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ItemRequestService itemRequestService;

    @SneakyThrows
    @Test
    void shouldSaveRequest() {
        Mockito.when(itemRequestService.create(Mockito.anyLong(), Mockito.any())).thenReturn(requestDto);

        ItemRequestCreationDto creationDto = new ItemRequestCreationDto(null, "test");

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(creationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].id", is(1L), Long.class))
                .andExpect(jsonPath("$.created", is(requestDto.getCreated()
                        .format(DEFAULT_DATE_TIME_FORMATTER))));
    }

    @SneakyThrows
    @Test
    void shouldFindById() {
        Mockito.when(itemRequestService.getById(Mockito.anyLong(), Mockito.anyLong())).thenReturn(requestDto);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.created", is(requestDto.getCreated()
                        .format(DEFAULT_DATE_TIME_FORMATTER))));
    }

    @SneakyThrows
    @Test
    void shouldFindByUser() {
        Mockito.when(itemRequestService.getByUser(Mockito.anyLong())).thenReturn(List.of(requestDto));

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.[0].items", hasSize(1)))
                .andExpect(jsonPath("$.[0].created", is(requestDto.getCreated()
                        .format(DEFAULT_DATE_TIME_FORMATTER))));
    }

    @SneakyThrows
    @Test
    void shouldFindAll() {
        Mockito.when(itemRequestService.getAll(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(requestDto));

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "1")
                        .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].description", is(requestDto.getDescription())))
                .andExpect(jsonPath("$.[0].items", hasSize(1)))
                .andExpect(jsonPath("$.[0].created", is(requestDto.getCreated()
                        .format(DEFAULT_DATE_TIME_FORMATTER))));
    }
}
