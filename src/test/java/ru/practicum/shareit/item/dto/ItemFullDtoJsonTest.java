package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemFullDtoJsonTest {
    @Autowired
    private JacksonTester<ItemFullDto> json;

    @Test
    void serializationTest() throws Exception {
        BookingShortDto lastBookingDto = BookingShortDto.builder().id(1L).bookerId(1L).build();
        LocalDateTime now = LocalDateTime.now();
        CommentFullDto comment = CommentFullDto.builder()
                .id(1L).text("test comment").authorName("Author").created(now).build();
        ItemFullDto itemFullDto = ItemFullDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .ownerId(1L)
                .requestId(1L)
                .lastBooking(lastBookingDto)
                .comments(List.of(comment))
                .build();

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
