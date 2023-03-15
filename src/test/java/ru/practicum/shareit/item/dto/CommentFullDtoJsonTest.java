package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.validation.util.ValidationUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.TestData.commentFullDto;


@JsonTest
public class CommentFullDtoJsonTest {
    @Autowired
    private JacksonTester<CommentFullDto> json;

    @SneakyThrows
    @Test
    void serializationTest() {
        JsonContent<CommentFullDto> jsonTest = json.write(commentFullDto);
        assertThat(jsonTest).extractingJsonPathStringValue("$.created", commentFullDto.getCreated()
                .format(ValidationUtil.DEFAULT_DATE_TIME_FORMATTER));
    }
}
