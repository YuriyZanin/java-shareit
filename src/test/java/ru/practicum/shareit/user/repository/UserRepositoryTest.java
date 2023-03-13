package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.AbstractRepositoryTest;
import ru.practicum.shareit.TestData;
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
        User newUser = TestData.getNewUser();

        userRepository.save(newUser);
        User fromDb = entityManager.getEntityManager()
                .createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", newUser.getEmail())
                .getSingleResult();

        assertThat(fromDb.getId(), notNullValue());
        assertThat(fromDb.getName(), equalTo(newUser.getName()));
        assertThat(fromDb.getEmail(), equalTo(newUser.getEmail()));
    }

    @Test
    void shouldFindAllUsers() {
        User test1 = User.builder().name("test1").email("test@email.com").build();
        User test2 = User.builder().name("test2").email("test2@email.com").build();
        User test3 = User.builder().name("test3").email("test3@email.com").build();

        entityManager.persist(test1);
        entityManager.persist(test2);
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
