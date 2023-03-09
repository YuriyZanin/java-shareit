package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.validation.util.ValidationUtil;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
public class CommentFullDtoJsonTest {
    @Autowired
    private JacksonTester<CommentFullDto> json;

    @Test
    void serializationTest() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        CommentFullDto comment = CommentFullDto.builder().id(1L).text("text").authorName("Author").created(now).build();

        JsonContent<CommentFullDto> jsonTest = json.write(comment);
        assertThat(jsonTest).extractingJsonPathNumberValue("$.id", comment.getId());
        assertThat(jsonTest).extractingJsonPathStringValue("$.text", comment.getText());
        assertThat(jsonTest).extractingJsonPathStringValue("$.authorName", comment.getAuthorName());
        assertThat(jsonTest).extractingJsonPathStringValue("$.created", comment.getCreated()
                .format(ValidationUtil.DEFAULT_DATE_TIME_FORMATTER));
    }
}
