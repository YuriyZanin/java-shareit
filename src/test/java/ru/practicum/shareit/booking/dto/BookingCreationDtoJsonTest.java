package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static ru.practicum.shareit.validation.util.ValidationUtil.DEFAULT_DATE_TIME_FORMATTER;

@JsonTest
public class BookingCreationDtoJsonTest {
    @Autowired
    private JacksonTester<BookingCreationDto> json;

    @SneakyThrows
    @Test
    void deserializationTest() {
        String start = LocalDateTime.now().format(DEFAULT_DATE_TIME_FORMATTER);
        String end = LocalDateTime.now().plusHours(1).format(DEFAULT_DATE_TIME_FORMATTER);
        String testString = String.format("{\n" +
                "    \"itemId\": 1,\n" +
                "    \"start\": \"%s\",\n" +
                "    \"end\": \"%s\"\n" +
                "}", start, end);

        BookingCreationDto creationDto = json.parseObject(testString);

        assertThat(creationDto.getItemId()).isEqualTo(1);
        assertThat(creationDto.getStart().format(DEFAULT_DATE_TIME_FORMATTER)).isEqualTo(start);
        assertThat(creationDto.getEnd().format(DEFAULT_DATE_TIME_FORMATTER)).isEqualTo(end);
    }
}
