package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.TestData.requestDto;
import static ru.practicum.shareit.validation.util.ValidationUtil.DEFAULT_DATE_TIME_FORMATTER;

@JsonTest
public class ItemRequestDtoJsonTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @SneakyThrows
    @Test
    void serializationTest() {
        JsonContent<ItemRequestDto> jsonContent = json.write(requestDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id", requestDto.getId());
        assertThat(jsonContent).extractingJsonPathStringValue("$.description", requestDto.getDescription());
        assertThat(jsonContent).extractingJsonPathStringValue("$.created", requestDto.getCreated()
                .format(DEFAULT_DATE_TIME_FORMATTER));
        assertThat(jsonContent).extractingJsonPathArrayValue("$.items", requestDto.getItems());
    }
}
