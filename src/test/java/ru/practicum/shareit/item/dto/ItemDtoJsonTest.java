package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.TestData.itemDto;

@JsonTest
public class ItemDtoJsonTest {
    @Autowired
    private JacksonTester<ItemDto> json;

    @SneakyThrows
    @Test
    void serializationTest() {
        JsonContent<ItemDto> jsonTest = json.write(itemDto);

        assertThat(jsonTest).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonTest).extractingJsonPathStringValue("$.description").isEqualTo(itemDto.getDescription());
        assertThat(jsonTest).extractingJsonPathBooleanValue("$.available").isTrue();
    }

    @SneakyThrows
    @Test
    void deserializationTest() {
        String testString = "{\"name\": \"test name\",\n" +
                "             \"description\": \"test description\",\n" +
                "             \"available\": true,\n" +
                "             \"requestId\": 1 }";

        ItemDto itemDto = json.parseObject(testString);

        assertThat(itemDto.getName()).isEqualTo("test name");
        assertThat(itemDto.getDescription()).isEqualTo("test description");
        assertThat(itemDto.getAvailable()).isTrue();
        assertThat(itemDto.getRequestId()).isEqualTo(1);
    }
}
