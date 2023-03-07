package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.AbstractRepositoryTest;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class UserRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveUser() {
        User test = userRepository.save(User.builder()
                .name("test")
                .email("test@email.com")
                .build());

        assertThat(test.getId(), notNullValue());
        assertThat(test.getName(), equalTo("test"));
        assertThat(test.getEmail(), equalTo("test@email.com"));
    }

    @Test
    void shouldFindAllUsers() {
        User test1 = User.builder().name("test1").email("test@email.com").build();
        entityManager.persist(test1);

        User test2 = User.builder().name("test2").email("test2@email.com").build();
        entityManager.persist(test2);

        User test3 = User.builder().name("test3").email("test3@email.com").build();
        entityManager.persist(test3);

        List<User> users = userRepository.findAll();
        assertThat(users, hasItems(test1, test2, test3));
    }

    @Test
    void shouldFindUserByEmail() {
        User test = User.builder().name("test").email("test@email.com").build();
        entityManager.persist(test);

        Optional<User> fromDb = userRepository.findUserByEmail("test@email.com");
        assertThat(fromDb, not(Optional.empty()));
        assertThat(fromDb, equalTo(Optional.of(test)));
    }
}
