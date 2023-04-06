package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ItemRequestServiceTest {
    private final ItemRequestService requestService;
    private final UserService userService;
    private final ItemService itemService;

    @Test
    void shouldSaveAndGetRequests() {
        UserDto user1Dto = UserDto.builder().name("user1").email("user1@mail.com").build();
        UserDto user2Dto = UserDto.builder().name("user2").email("user2@mail.com").build();
        ItemRequestCreationDto request1Dto = new ItemRequestCreationDto(null, "description 1");
        ItemRequestCreationDto request2Dto = new ItemRequestCreationDto(null, "description 2");
        ItemRequestCreationDto request3Dto = new ItemRequestCreationDto(null, "description 3");

        UserDto createdUser1 = userService.create(user1Dto);
        UserDto createdUser2 = userService.create(user2Dto);
        ItemRequestDto createdRequest1 = requestService.create(createdUser1.getId(), request1Dto);
        ItemRequestDto createdRequest2 = requestService.create(createdUser1.getId(), request2Dto);
        ItemRequestDto createdRequest3 = requestService.create(createdUser1.getId(), request3Dto);
        ItemRequestDto request1FromDb = requestService.getById(createdUser1.getId(), createdRequest1.getId());
        ItemRequestDto request2FromDb = requestService.getById(createdUser1.getId(), createdRequest2.getId());
        ItemRequestDto request3FromDb = requestService.getById(createdUser1.getId(), createdRequest3.getId());

        ItemDto itemDto = ItemDto.builder()
                .name("item1").description("item for request 1").available(true).requestId(createdRequest1.getId())
                .build();
        itemService.create(createdUser2.getId(), itemDto);
        ItemRequestDto request1AfterAddItem = requestService.getById(createdUser1.getId(), createdRequest1.getId());

        List<ItemRequestDto> allByUser1 = requestService.getByUser(createdUser1.getId());
        List<ItemRequestDto> allForUser1 = requestService.getAll(createdUser1.getId(), 0, 3);
        List<ItemRequestDto> allForUser2 = requestService.getAll(createdUser2.getId(), 0, 3);

        assertThat(request1FromDb, equalTo(createdRequest1));
        assertThat(request2FromDb, equalTo(createdRequest2));
        assertThat(request3FromDb, equalTo(createdRequest3));
        assertThat(request1AfterAddItem.getItems(), not(empty()));
        assertThat(request1AfterAddItem.getItems(), hasSize(1));
        assertThat(request1AfterAddItem.getItems().get(0).getRequestId(), equalTo(createdRequest1.getId()));
        assertThat(allByUser1, not(empty()));
        assertThat(allByUser1, hasSize(3));
        assertThat(allByUser1, hasItems(request1AfterAddItem, createdRequest2, createdRequest3));
        assertThat(allForUser1, empty());
        assertThat(allForUser2, not(empty()));
        assertThat(allForUser2, hasItems(request1AfterAddItem, createdRequest2, createdRequest3));
    }
}
