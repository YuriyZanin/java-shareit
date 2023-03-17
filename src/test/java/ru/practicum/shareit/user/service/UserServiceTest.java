package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UserServiceTest {
    private final UserService userService;

    @Test
    void shouldSaveAndGetUsers() {
        UserDto user1Dto = UserDto.builder().name("user1").email("user1@mail.com").build();
        UserDto user2Dto = UserDto.builder().name("user2").email("user2@mail.com").build();
        UserDto user3Dto = UserDto.builder().name("user3").email("user3@mail.com").build();
        UserDto updateDto = UserDto.builder().name("updated").email("updated@mail.com").build();

        UserDto createdUser1 = userService.create(user1Dto);
        UserDto createdUser2 = userService.create(user2Dto);
        UserDto createdUser3 = userService.create(user3Dto);

        UserDto user1FromDb = userService.get(createdUser1.getId());
        UserDto user2FromDb = userService.get(createdUser2.getId());
        UserDto user3FromDb = userService.get(createdUser3.getId());

        UserDto updatedUser1 = userService.update(createdUser1.getId(), updateDto);
        UserDto user1AfterUpdate = userService.get(createdUser1.getId());

        List<UserDto> all = userService.getAll();

        userService.delete(createdUser1.getId());
        List<UserDto> allAfterDelete = userService.getAll();

        assertThat(user1FromDb, equalTo(createdUser1));
        assertThat(user2FromDb, equalTo(createdUser2));
        assertThat(user3FromDb, equalTo(createdUser3));
        assertThat(updatedUser1, equalTo(user1AfterUpdate));
        assertThat(all, not(empty()));
        assertThat(all, hasSize(3));
        assertThat(all, hasItems(updatedUser1, createdUser2, createdUser3));
        assertThat(allAfterDelete, not(empty()));
        assertThat(allAfterDelete, hasSize(2));
        assertThat(allAfterDelete, hasItems(createdUser2, createdUser3));
    }
}
