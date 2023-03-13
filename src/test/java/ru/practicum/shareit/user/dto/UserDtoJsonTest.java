package ru.practicum.shareit.user.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.TestData.userDto;


@JsonTest
public class UserDtoJsonTest {
    @Autowired
    private JacksonTester<UserDto> json;

    @SneakyThrows
    @Test
    void serializationTest() {
        JsonContent<UserDto> testJson = json.write(userDto);

        assertThat(testJson).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(testJson).extractingJsonPathStringValue("$.name").isEqualTo("test");
        assertThat(testJson).extractingJsonPathStringValue("$.email").isEqualTo("test@mail.com");
    }

    @SneakyThrows
    @Test
    void deserializationTest() {
        String testString = "{\"name\": \"user\", \"email\": \"user@user.com\" }";

        UserDto userDto = json.parseObject(testString);

        assertThat(userDto.getName()).isEqualTo("user");
        assertThat(userDto.getEmail()).isEqualTo("user@user.com");
    }
}
