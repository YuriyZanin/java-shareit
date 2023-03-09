package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.ObjectContent;

@JsonTest
public class ItemRequestCreationDtoJsonTest {
    @Autowired
    protected JacksonTester<ItemRequestCreationDto> json;

    @Test
    void deserializationTest() throws Exception {
        String testString = "{ \"description\": \"test description\" }";

        ObjectContent<ItemRequestCreationDto> creationDto = json.parse(testString);
        creationDto.assertThat().isExactlyInstanceOf(ItemRequestCreationDto.class);
    }
}
