package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.AbstractRepositoryTest;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ItemRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;

    @Test
    void shouldSaveItem() {
        User owner = TestData.getNewUser();
        Item newItem = TestData.getNewItem(owner);

        entityManager.persist(owner);
        Item saved = itemRepository.save(newItem);
        Item fromDb = entityManager.getEntityManager()
                .createQuery("SELECT i FROM Item i WHERE i.id = :itemId", Item.class)
                .setParameter("itemId", saved.getId())
                .getSingleResult();

        assertThat(newItem.getId(), equalTo(saved.getId()));
        assertThat(newItem.getName(), equalTo(fromDb.getName()));
        assertThat(newItem.getDescription(), equalTo(fromDb.getDescription()));
        assertThat(newItem.getAvailable(), equalTo(fromDb.getAvailable()));
        assertThat(newItem.getOwner(), equalTo(owner));
        assertThat(newItem.getRequest(), nullValue());
        assertThat(newItem.getComments(), nullValue());
    }

    @Test
    void shouldFindByOwner() {
        User owner = TestData.getNewUser();
        Item item = TestData.getNewItem(owner);

        entityManager.persist(owner);
        entityManager.persist(item);
        List<Item> itemsByOwner = itemRepository.findByOwnerId(PageRequest.of(0, 2), owner.getId())
                .getContent();
        Optional<Item> itemsByIdAndOwner = itemRepository.findByIdAndOwnerId(item.getId(), owner.getId());
        Optional<Item> itemsByIdOtherOwner = itemRepository.findByIdAndOwnerIdNot(item.getId(), owner.getId());

        assertThat(itemsByOwner, hasSize(1));
        assertThat(itemsByOwner, hasItem(item));
        assertThat(itemsByIdAndOwner, is(not(Optional.empty())));
        assertThat(itemsByIdAndOwner, equalTo(Optional.of(item)));
        assertThat(itemsByIdOtherOwner, equalTo(Optional.empty()));
    }

    @Test
    void shouldFindByText() {
        User owner = TestData.getNewUser();
        Item testItem = TestData.getNewItem(owner);

        entityManager.persist(owner);
        entityManager.persist(testItem);
        List<Item> itemsByName = itemRepository.searchByText(PageRequest.of(0, 1), "name").getContent();
        List<Item> itemsByDesc = itemRepository.searchByText(PageRequest.of(0, 1), "description")
                .getContent();

        assertThat(itemsByName, hasSize(1));
        assertThat(itemsByName, hasItem(testItem));
        assertThat(itemsByDesc, hasSize(1));
        assertThat(itemsByDesc, hasItem(testItem));
    }

    @Test
    void shouldFindByRequest() {
        User owner = User.builder().name("test").email("test@mail.com").build();
        User requester = User.builder().name("requester").email("test2@mail.com").build();
        ItemRequest testRequest = TestData.getNewItemRequest(requester);
        Item testItem = Item.builder()
                .name("test").description("desc").available(true).owner(owner).request(testRequest).build();

        entityManager.persist(owner);
        entityManager.persist(requester);
        entityManager.persist(testRequest);
        entityManager.persist(testItem);
        List<Item> itemsByRequest = itemRepository.findByRequestId(testRequest.getId());
        List<Item> itemsByRequestsIn = itemRepository.findByRequestIdIn(List.of(testRequest.getId()));

        assertThat(itemsByRequest, hasSize(1));
        assertThat(itemsByRequest, hasItem(testItem));
        assertThat(itemsByRequestsIn, hasSize(1));
        assertThat(itemsByRequestsIn, hasItem(testItem));
    }

    @Test
    void shouldDeleteByOwner() {
        User owner = TestData.getNewUser();
        Item test = TestData.getNewItem(owner);

        entityManager.persist(owner);
        entityManager.persist(test);
        itemRepository.deleteByIdAndOwnerId(test.getId(), owner.getId());
        Optional<Item> item = itemRepository.findByIdAndOwnerId(test.getId(), owner.getId());
        List<Item> resultList = entityManager.getEntityManager()
                .createQuery("SELECT i FROM Item i WHERE i.id = :itemId", Item.class)
                .setParameter("itemId", test.getId())
                .getResultList();

        assertThat(item, is(Optional.empty()));
        assertThat(resultList, empty());
    }
}
