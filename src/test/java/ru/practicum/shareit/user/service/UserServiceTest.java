package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.practicum.shareit.TestData.getNewUser;

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
    void shouldBeFailedIfAlreadyExist() {
        User user = getNewUser();
        UserDto userDto2 = UserDto.builder().name("test2").email(user.getEmail()).build();

        em.persist(user);

        Assertions.assertThrows(AlreadyExistException.class, () -> userService.create(userDto2));
    }

    @Test
    void shouldUpdateUser() {
        User user = getNewUser();

        em.persist(user);
        UserDto updateDto = UserDto.builder().name("updated name").email("update@mail.com").build();
        userService.update(user.getId(), updateDto);
        User fromDb = em.createQuery("SELECT u FROM User u WHERE u.id = :userId", User.class)
                .setParameter("userId", user.getId())
                .getSingleResult();

        assertThat(fromDb.getId(), equalTo(user.getId()));
        assertThat(fromDb.getName(), equalTo(updateDto.getName()));
        assertThat(fromDb.getEmail(), equalTo(updateDto.getEmail()));
    }

    @Test
    void shouldFindAll() {
        User user = getNewUser();

        em.persist(user);
        UserDto userDto = UserMapper.toUserDto(user);
        List<UserDto> all = userService.getAll();

        assertThat(all, hasSize(1));
        assertThat(all, hasItem(userDto));
    }

    @Test
    void shouldFindById() {
        User user = getNewUser();

        em.persist(user);
        UserDto fromDb = userService.get(user.getId());
        UserDto userDto = UserMapper.toUserDto(user);

        assertThat(fromDb, equalTo(userDto));
    }

    @Test
    void shouldDeleteById() {
        User user = getNewUser();

        em.persist(user);
        userService.delete(user.getId());
        List<User> result = em.createQuery("SELECT u FROM User u WHERE u.email = 'test@mail.com'", User.class)
                .getResultList();

        assertThat(result, hasSize(0));
    }
}
