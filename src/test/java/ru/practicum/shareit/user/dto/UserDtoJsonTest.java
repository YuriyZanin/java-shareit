package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
public class UserDtoJsonTest {
    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    void serializationTest() throws Exception {
        UserDto test = UserDto.builder().id(1L).name("test").email("test@mail.com").build();

        JsonContent<UserDto> testJson = json.write(test);
        assertThat(testJson).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(testJson).extractingJsonPathStringValue("$.name").isEqualTo("test");
        assertThat(testJson).extractingJsonPathStringValue("$.email").isEqualTo("test@mail.com");
    }

    @Test
    void deserializationTest() throws Exception {
        String testString = "{\"name\": \"user\", \"email\": \"user@user.com\" }";

        UserDto userDto = json.parseObject(testString);
        assertThat(userDto.getName()).isEqualTo("user");
        assertThat(userDto.getEmail()).isEqualTo("user@user.com");
    }
}
