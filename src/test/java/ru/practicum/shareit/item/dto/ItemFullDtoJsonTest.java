package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.TestData.itemFullDto;

@JsonTest
public class ItemFullDtoJsonTest {
    @Autowired
    private JacksonTester<ItemFullDto> json;

    @SneakyThrows
    @Test
    void serializationTest() {
        JsonContent<ItemFullDto> jsonTest = json.write(itemFullDto);

        assertThat(jsonTest).extractingJsonPathNumberValue("$.id", itemFullDto.getId());
        assertThat(jsonTest).extractingJsonPathStringValue("$.name", itemFullDto.getName());
        assertThat(jsonTest).extractingJsonPathStringValue("$.description", itemFullDto.getDescription());
        assertThat(jsonTest).extractingJsonPathBooleanValue("$.available", itemFullDto.getAvailable());
        assertThat(jsonTest).extractingJsonPathNumberValue("$.ownerId", itemFullDto.getOwnerId());
        assertThat(jsonTest).extractingJsonPathNumberValue("$.requestId", itemFullDto.getRequestId());
        assertThat(jsonTest).extractingJsonPathNumberValue("$.lastBooking.id",
                itemFullDto.getLastBooking().getId());
        assertThat(jsonTest).extractingJsonPathNumberValue("$.lastBooking.bookerId",
                itemFullDto.getLastBooking().getBookerId());
        assertThat(jsonTest).hasEmptyJsonPathValue("$.nextBooking");
        assertThat(jsonTest).extractingJsonPathArrayValue("$.comments", itemFullDto.getComments());
    }
}
