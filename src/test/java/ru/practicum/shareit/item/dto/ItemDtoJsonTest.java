package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoJsonTest {
    @Autowired
    private JacksonTester<ItemDto> json;

    @Test
    void serializationTest() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1L).name("test").description("description").available(true).requestId(2L).build();

        JsonContent<ItemDto> jsonTest = json.write(itemDto);
        assertThat(jsonTest).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonTest).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(jsonTest).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(jsonTest).extractingJsonPathNumberValue("$.requestId").isEqualTo(2);
    }

    @Test
    void deserializationTest() throws Exception {
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
