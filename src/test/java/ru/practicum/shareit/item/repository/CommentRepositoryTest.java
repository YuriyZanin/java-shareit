package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.AbstractRepositoryTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static ru.practicum.shareit.TestData.*;

public class CommentRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;

    @Test
    void shouldSaveComment() {
        User owner = getNewUser();
        Item newItem = getNewItem(owner);
        User author = User.builder().name("author").email("author@mail.com").build();
        Comment comment = Comment.builder().item(newItem).author(author).text("test").created(currentDateTime).build();

        entityManager.persist(author);
        entityManager.persist(owner);
        entityManager.persist(newItem);
        commentRepository.save(comment);

        Comment fromDb = entityManager.getEntityManager()
                .createQuery("SELECT c FROM Comment c WHERE c.author.id = :authorId AND c.item.id = :itemId",
                        Comment.class)
                .setParameter("authorId", author.getId())
                .setParameter("itemId", newItem.getId())
                .getSingleResult();

        assertThat(fromDb.getId(), notNullValue());
        assertThat(comment.getText(), equalTo(fromDb.getText()));
        assertThat(comment.getItem(), equalTo(fromDb.getItem()));
        assertThat(comment.getAuthor(), equalTo(fromDb.getAuthor()));
        assertThat(comment.getCreated(), equalTo(fromDb.getCreated()));
    }
}
