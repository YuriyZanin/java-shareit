package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.AbstractRepositoryTest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.practicum.shareit.TestData.*;

public class RequestRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    void shouldSaveRequest() {
        User requester = getNewUser();
        ItemRequest newRequest = getNewItemRequest(requester);

        entityManager.persist(requester);
        ItemRequest saved = itemRequestRepository.save(newRequest);
        ItemRequest fromDb = entityManager.getEntityManager()
                .createQuery("SELECT r FROM ItemRequest r WHERE r.id = :requestId", ItemRequest.class)
                .setParameter("requestId", saved.getId())
                .getSingleResult();

        assertThat(fromDb.getId(), equalTo(saved.getId()));
        assertThat(fromDb.getRequester(), equalTo(requester));
        assertThat(fromDb.getDescription(), equalTo(newRequest.getDescription()));
        assertThat(fromDb.getCreated(), equalTo(newRequest.getCreated()));
    }

    @Test
    void shouldFindByRequester() {
        User requester = getNewUser();
        ItemRequest request = getNewItemRequest(requester);

        entityManager.persist(requester);
        entityManager.persist(request);
        List<ItemRequest> requests = itemRequestRepository.findByRequesterId(requester.getId(),
                Sort.by("created"));

        assertThat(requests, hasSize(1));
        assertThat(requests, hasItem(request));
    }

    @Test
    void shouldFindAllRequestOfOtherUsers() {
        User requester1 = User.builder().name("user1").email("test1@mail.com").build();
        User requester2 = User.builder().name("user2").email("test2@mail.com").build();
        ItemRequest request1 = ItemRequest.builder().description("test1").requester(requester1).created(currentDateTime)
                .build();
        ItemRequest request2 = ItemRequest.builder().description("test2").requester(requester2).created(currentDateTime)
                .build();

        entityManager.persist(requester1);
        entityManager.persist(request1);
        entityManager.persist(requester2);
        entityManager.persist(request2);
        List<ItemRequest> requestsForUser1 = itemRequestRepository.findByRequesterIdNot(PageRequest.of(0, 2),
                requester1.getId()).getContent();
        List<ItemRequest> requestsForUser2 = itemRequestRepository.findByRequesterIdNot(PageRequest.of(0, 2),
                requester2.getId()).getContent();

        assertThat(requestsForUser1, notNullValue());
        assertThat(requestsForUser1, hasSize(1));
        assertThat(requestsForUser1, hasItem(request2));
        assertThat(requestsForUser2, notNullValue());
        assertThat(requestsForUser2, hasSize(1));
        assertThat(requestsForUser2, hasItem(request1));
    }
}
