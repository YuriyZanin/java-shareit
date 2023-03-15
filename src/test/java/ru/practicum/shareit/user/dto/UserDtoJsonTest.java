package ru.practicum.shareit.user.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
public class UserDtoJsonTest {
    @Autowired
    private JacksonTester<UserDto> json;

    @SneakyThrows
    @Test
    void deserializationTest() {
        String testString = "{\"name\": \"user\", \"email\": \"user@user.com\" }";

        UserDto userDto = json.parseObject(testString);

        assertThat(userDto.getName()).isEqualTo("user");
        assertThat(userDto.getEmail()).isEqualTo("user@user.com");
    }
}
