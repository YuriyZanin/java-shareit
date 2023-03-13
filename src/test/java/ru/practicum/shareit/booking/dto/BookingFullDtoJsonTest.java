package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.mapper.BookingMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.TestData.booking1OfItem3;
import static ru.practicum.shareit.validation.util.ValidationUtil.DEFAULT_DATE_TIME_FORMATTER;

@JsonTest
public class BookingFullDtoJsonTest {
    @Autowired
    private JacksonTester<BookingFullDto> json;

    @SneakyThrows
    @Test
    void serializationTest() {
        BookingFullDto fullDto = BookingMapper.toBookingFullDto(booking1OfItem3);

        JsonContent<BookingFullDto> jsonTest = json.write(fullDto);

        assertThat(jsonTest).extractingJsonPathNumberValue("$.id", fullDto.getId());
        assertThat(jsonTest).extractingJsonPathStringValue("$.start", booking1OfItem3.getStart()
                .format(DEFAULT_DATE_TIME_FORMATTER));
        assertThat(jsonTest).extractingJsonPathStringValue("$.end", booking1OfItem3.getEnd()
                .format(DEFAULT_DATE_TIME_FORMATTER));
        assertThat(jsonTest).extractingJsonPathStringValue("$.status", fullDto.getStatus().name());
        assertThat(jsonTest).extractingJsonPathValue("$.booker", fullDto.getBooker());
        assertThat(jsonTest).extractingJsonPathValue("$.item", fullDto.getItem());
    }
}
