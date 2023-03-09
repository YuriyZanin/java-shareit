package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareit.validation.util.ValidationUtil.DEFAULT_DATE_TIME_FORMATTER;

@JsonTest
public class ItemRequestDtoJsonTest {
    @Autowired
    protected JacksonTester<ItemRequestDto> json;

    @Test
    void serializationTest() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        ItemDto item = ItemDto.builder().id(1L).requestId(2L).available(true).name("name").description("test").build();
        ItemRequestDto requestDto = ItemRequestDto.builder()
                .id(1L)
                .description("description")
                .created(now)
                .items(List.of(item))
                .build();

        JsonContent<ItemRequestDto> jsonContent = json.write(requestDto);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.id", requestDto.getId());
        assertThat(jsonContent).extractingJsonPathStringValue("$.description", requestDto.getDescription());
        assertThat(jsonContent).extractingJsonPathStringValue("$.created", requestDto.getCreated()
                .format(DEFAULT_DATE_TIME_FORMATTER));
        assertThat(jsonContent).extractingJsonPathArrayValue("$.items", requestDto.getItems());
    }
}
