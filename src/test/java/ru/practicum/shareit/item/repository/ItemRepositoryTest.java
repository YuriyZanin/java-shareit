package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.AbstractRepositoryTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ItemRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void shouldSaveItem() {
        User owner = User.builder().name("user").email("test@mail.com").build();
        entityManager.persist(owner);

        Item test = itemRepository.save(
                Item.builder().name("test").description("desc").available(true).owner(owner).build());

        assertThat(test.getId(), notNullValue());
        assertThat(test.getName(), equalTo("test"));
        assertThat(test.getDescription(), equalTo("desc"));
        assertThat(test.getAvailable(), equalTo(true));
        assertThat(test.getOwner(), equalTo(owner));
        assertThat(test.getRequest(), nullValue());
        assertThat(test.getComments(), nullValue());
    }

    @Test
    void shouldFindByOwner() {
        User owner = User.builder().name("test").email("test@mail.com").build();
        entityManager.persist(owner);

        Item testItem = Item.builder().name("test").description("desc").available(true).owner(owner).build();
        entityManager.persist(testItem);

        Page<Item> page = itemRepository.findByOwnerId(PageRequest.of(0, 1), owner.getId());
        assertThat(page, notNullValue());

        List<Item> content = page.getContent();
        assertThat(content, hasSize(1));
        assertThat(content, hasItem(testItem));

        Optional<Item> item = itemRepository.findByIdAndOwnerId(testItem.getId(), owner.getId());
        assertThat(item, is(not(Optional.empty())));
        assertThat(item, equalTo(Optional.of(testItem)));

        item = itemRepository.findByIdAndOwnerIdNot(testItem.getId(), owner.getId());
        assertThat(item, equalTo(Optional.empty()));

        item = itemRepository.findByIdAndOwnerIdNot(testItem.getId(), 999L);
        assertThat(item, is(not(Optional.empty())));
        assertThat(item, equalTo(Optional.of(testItem)));
    }

    @Test
    void shouldFindByText() {
        User owner = User.builder().name("test").email("test@mail.com").build();
        entityManager.persist(owner);

        Item testItem = Item.builder().name("test").description("desc").available(true).owner(owner).build();
        entityManager.persist(testItem);

        Page<Item> page = itemRepository.searchByText(PageRequest.of(0, 1), "test");
        assertThat(page, notNullValue());

        List<Item> content = page.getContent();
        assertThat(content, hasSize(1));
        assertThat(content, hasItem(testItem));

        page = itemRepository.searchByText(PageRequest.of(0, 1), "desc");
        assertThat(content, hasSize(1));
        assertThat(content, hasItem(testItem));
    }

    @Test
    void shouldFindByRequest() {
        User owner = User.builder().name("test").email("test@mail.com").build();
        entityManager.persist(owner);

        User requester = User.builder().name("requester").email("test2@mail.com").build();
        entityManager.persist(requester);

        ItemRequest testRequest = ItemRequest.builder()
                .description("test request").requester(requester).created(LocalDateTime.now()).build();
        entityManager.persist(testRequest);

        Item testItem = Item.builder()
                .name("test").description("desc").available(true).owner(owner).request(testRequest).build();
        entityManager.persist(testItem);

        List<Item> items = itemRepository.findByRequestId(testRequest.getId());
        assertThat(items, hasSize(1));
        assertThat(items, hasItem(testItem));

        items = itemRepository.findByRequestIdIn(List.of(testRequest.getId()));
        assertThat(items, hasSize(1));
        assertThat(items, hasItem(testItem));
    }

    @Test
    void shouldDeleteByOwner() {
        User owner = User.builder().name("user").email("test@mail.com").build();
        entityManager.persist(owner);

        Item test = Item.builder().name("test").description("desc").available(true).owner(owner).build();
        entityManager.persist(test);

        itemRepository.deleteByIdAndOwnerId(test.getId(), owner.getId());
        Optional<Item> item = itemRepository.findByIdAndOwnerId(test.getId(), owner.getId());
        assertThat(item, is(Optional.empty()));
    }
}
