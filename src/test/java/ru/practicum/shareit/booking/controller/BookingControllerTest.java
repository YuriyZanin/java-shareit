package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private final LocalDateTime startTime = LocalDateTime.now().plusHours(1);
    private final LocalDateTime endTime = LocalDateTime.now().plusHours(2);

    private final BookingFullDto bookingDto = BookingFullDto.builder()
            .id(1L)
            .booker(UserDto.builder().id(1L).name("test").email("test@mail.com").build())
            .item(ItemFullDto.builder()
                    .id(1L).available(true).description("description").name("name").ownerId(2L).requestId(1L).build())
            .start(startTime)
            .end(endTime)
            .status(Status.WAITING)
            .build();

    @Test
    void shouldSaveNewBooking() throws Exception {
        Mockito.when(bookingService.create(Mockito.anyLong(), Mockito.any())).thenReturn(bookingDto);
        BookingCreationDto creationDto = BookingCreationDto.builder()
                .itemId(1L)
                .start(startTime)
                .end(endTime)
                .build();


        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(creationDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().format(dateFormat))))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().format(dateFormat))))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().name())));
    }

    @Test
    void shouldApproveStatus() throws Exception {
        BookingFullDto approved = BookingFullDto.builder().id(bookingDto.getId())
                .booker(bookingDto.getBooker())
                .item(bookingDto.getItem())
                .start(startTime)
                .end(endTime)
                .status(Status.APPROVED)
                .build();
        Mockito.when(bookingService.approveStatus(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean()))
                .thenReturn(approved);

        mvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.start", is(approved.getStart().format(dateFormat))))
                .andExpect(jsonPath("$.end", is(approved.getEnd().format(dateFormat))))
                .andExpect(jsonPath("$.status", is(approved.getStatus().name())));
    }

    @Test
    void shouldFindBookingById() throws Exception {
        Mockito.when(bookingService.get(Mockito.anyLong(), Mockito.anyLong())).thenReturn(bookingDto);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().format(dateFormat))))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().format(dateFormat))))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().name())));
    }

    @Test
    void shouldFindByState() throws Exception {
        Mockito.when(bookingService.getAllByState(Mockito.anyLong(), Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.[0].booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[0].start", is(bookingDto.getStart().format(dateFormat))))
                .andExpect(jsonPath("$.[0].end", is(bookingDto.getEnd().format(dateFormat))))
                .andExpect(jsonPath("$.[0].status", is(bookingDto.getStatus().name())));
    }

    @Test
    void shouldFindByOwnerAndState() throws Exception {
        Mockito.when(bookingService.getAllByOwnerAndState(
                Mockito.anyLong(), Mockito.any(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", isA(ArrayList.class)))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].item.id", is(bookingDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.[0].booker.id", is(bookingDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[0].start", is(bookingDto.getStart().format(dateFormat))))
                .andExpect(jsonPath("$.[0].end", is(bookingDto.getEnd().format(dateFormat))))
                .andExpect(jsonPath("$.[0].status", is(bookingDto.getStatus().name())));
    }
}
