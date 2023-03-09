package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JsonTest
public class CommentCreationDtoJsonTest {
    @Autowired
    private JacksonTester<CommentCreationDto> json;

    @Test
    void deserializationTest() throws Exception {
        String testString = "{\"text\": \"test comment\" }";

        CommentCreationDto commentCreationDto = json.parseObject(testString);
        assertThat(commentCreationDto.getId()).isNull();
        assertThat(commentCreationDto.getText()).isEqualTo("test comment");
    }
}
