package ru.practicum.shareit.user.sevice;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    private final EntityManager em;
    private final UserService userService;

    @Test
    void shouldSaveUser() {
        UserDto userDto = UserDto.builder().name("test").email("test@mail.com").build();
        userService.create(userDto);

        User result = em.createQuery("SELECT u FROM User u WHERE u.email = 'test@mail.com'", User.class)
                .getSingleResult();

        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(userDto.getName()));
        assertThat(result.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void shouldFindAll() {
        User user = User.builder().name("test").email("test@mail.com").build();
        em.persist(user);

        UserDto userDto = UserMapper.toUserDto(user);
        List<UserDto> all = userService.getAll();
        assertThat(all, hasSize(1));
        assertThat(all, hasItem(userDto));
    }

    @Test
    void shouldFindById() {
        User user = User.builder().name("test").email("test@mail.com").build();
        em.persist(user);

        UserDto fromDb = userService.get(user.getId());
        UserDto userDto = UserMapper.toUserDto(user);
        assertThat(fromDb, equalTo(userDto));
    }

    @Test
    void shouldDeleteById() {
        User user = User.builder().name("test").email("test@mail.com").build();
        em.persist(user);

        userService.delete(user.getId());
        List<User> result = em.createQuery("SELECT u FROM User u WHERE u.email = 'test@mail.com'", User.class)
                .getResultList();

        assertThat(result, hasSize(0));
    }
}
