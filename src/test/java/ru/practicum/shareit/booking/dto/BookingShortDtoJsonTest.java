package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingShortDtoJsonTest {
    @Autowired
    private JacksonTester<BookingShortDto> json;

    @SneakyThrows
    @Test
    void serializationTest() {
        BookingShortDto shortDto = BookingShortDto.builder().id(1L).bookerId(2L).build();

        JsonContent<BookingShortDto> jsonTest = json.write(shortDto);

        assertThat(jsonTest).extractingJsonPathNumberValue("$.id", shortDto.getId());
        assertThat(jsonTest).extractingJsonPathNumberValue("$.bookerId", shortDto.getBookerId());
    }
}
