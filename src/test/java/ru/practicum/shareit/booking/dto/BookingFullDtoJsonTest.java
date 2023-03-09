package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.validation.util.ValidationUtil.DEFAULT_DATE_TIME_FORMATTER;

@JsonTest
public class BookingFullDtoJsonTest {
    @Autowired
    private JacksonTester<BookingFullDto> json;

    @Test
    void serializationTest() throws Exception {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        BookingFullDto fullDto = BookingFullDto.builder()
                .id(1L)
                .start(start)
                .end(end)
                .status(Status.WAITING)
                .booker(UserDto.builder().id(1L).name("user").email("user@mail.com").build())
                .item(ItemFullDto.builder().id(1L).ownerId(2L).name("name").description("descr").available(true).build())
                .build();

        JsonContent<BookingFullDto> jsonTest = json.write(fullDto);
        assertThat(jsonTest).extractingJsonPathNumberValue("$.id", fullDto.getId());
        assertThat(jsonTest).extractingJsonPathStringValue("$.start", start.format(DEFAULT_DATE_TIME_FORMATTER));
        assertThat(jsonTest).extractingJsonPathStringValue("$.end", end.format(DEFAULT_DATE_TIME_FORMATTER));
        assertThat(jsonTest).extractingJsonPathStringValue("$.status", fullDto.getStatus().name());
        assertThat(jsonTest).extractingJsonPathValue("$.booker", fullDto.getBooker());
        assertThat(jsonTest).extractingJsonPathValue("$.item", fullDto.getItem());
    }
}
