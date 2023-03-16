package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.practicum.shareit.TestData.getNewUser;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ItemServiceTest {
    private final ItemService itemService;
    private final UserService userService;

    @SneakyThrows
    @Test
    void shouldSaveAndGetItems() {
        User owner = getNewUser();
        ItemDto item1Dto = ItemDto.builder().name("item1").description("description 1").available(true).build();
        ItemDto item2Dto = ItemDto.builder().name("item2").description("description 2").available(true).build();
        ItemDto item3Dto = ItemDto.builder().name("item3").description("description 3").available(true).build();
        ItemDto updateDto = ItemDto.builder()
                .name("updated name").available(false).description("updated description").build();

        UserDto createdOwner = userService.create(UserMapper.toUserDto(owner));

        ItemFullDto createdItem1 = itemService.create(createdOwner.getId(), item1Dto);
        ItemFullDto createdItem2 = itemService.create(createdOwner.getId(), item2Dto);
        ItemFullDto createdItem3 = itemService.create(createdOwner.getId(), item3Dto);

        ItemFullDto item1FromDb = itemService.get(createdOwner.getId(), createdItem1.getId());
        ItemFullDto item2FromDb = itemService.get(createdOwner.getId(), createdItem2.getId());
        ItemFullDto item3FromDb = itemService.get(createdOwner.getId(), createdItem3.getId());

        List<ItemFullDto> allByUser = itemService.getAllByUser(createdOwner.getId(), 0, 3);
        List<ItemFullDto> allByDescription = itemService.getByText(createdOwner.getId(),
                "description", 0, 3);
        List<ItemFullDto> allByName = itemService.getByText(createdOwner.getId(), "item", 0, 3);
        List<ItemFullDto> emptyText = itemService.getByText(createdOwner.getId(), "", 0, 1);

        UserDto otherUser = userService.create(UserDto.builder().name("other").email("other@mail.com").build());
        ItemFullDto updatedItem1 = itemService.update(createdOwner.getId(), createdItem1.getId(), updateDto);
        ItemFullDto item1AfterUpdate = itemService.get(createdOwner.getId(), createdItem1.getId());

        itemService.delete(createdOwner.getId(), createdItem1.getId());
        List<ItemFullDto> allAfterDelete = itemService.getAllByUser(createdOwner.getId(), 0, 3);

        assertThat(item1FromDb, equalTo(createdItem1));
        assertThat(item2FromDb, equalTo(createdItem2));
        assertThat(item3FromDb, equalTo(createdItem3));
        assertThat(allByUser, not(empty()));
        assertThat(allByUser, hasSize(3));
        assertThat(allByUser, hasItems(createdItem1, createdItem2, createdItem3));
        assertThat(allByDescription, not(empty()));
        assertThat(allByDescription, hasSize(3));
        assertThat(allByDescription, hasItems(createdItem1, createdItem2, createdItem3));
        assertThat(allByName, not(empty()));
        assertThat(allByName, hasSize(3));
        assertThat(allByName, hasItems(createdItem1, createdItem2, createdItem3));
        assertThat(emptyText, empty());
        assertThat(updatedItem1, equalTo(item1AfterUpdate));
        assertThat(allAfterDelete, not(empty()));
        assertThat(allAfterDelete, hasSize(2));
    }
}
