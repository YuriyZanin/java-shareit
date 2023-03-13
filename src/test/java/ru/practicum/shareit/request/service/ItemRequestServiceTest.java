package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.TestData;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {
    private final EntityManager em;
    private final ItemRequestService requestService;

    @Test
    void shouldSaveRequest() {
        User user = TestData.getNewUser();
        ItemRequestCreationDto creationDto = new ItemRequestCreationDto(null, "description");

        em.persist(user);
        ItemRequestDto saved = requestService.create(user.getId(), creationDto);
        ItemRequest fromDb = em.createQuery("SELECT i FROM ItemRequest i WHERE i.id = :requestId", ItemRequest.class)
                .setParameter("requestId", saved.getId())
                .getSingleResult();

        assertThat(fromDb.getId(), notNullValue());
        assertThat(fromDb.getRequester(), equalTo(user));
        assertThat(fromDb.getDescription(), equalTo(creationDto.getDescription()));
    }

    @Test
    void shouldFindByUser() {
        User user = TestData.getNewUser();
        ItemRequest request = TestData.getNewItemRequest(user);

        em.persist(user);
        em.persist(request);
        List<ItemRequestDto> requests = requestService.getByUser(user.getId());
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(request, Collections.emptyList());

        assertThat(requests, hasSize(1));
        assertThat(requests, hasItem(itemRequestDto));
    }

    @Test
    void shouldFindById() {
        User user = TestData.getNewUser();
        ItemRequest request = TestData.getNewItemRequest(user);

        em.persist(user);
        em.persist(request);
        ItemRequestDto fromDb = requestService.getById(user.getId(), request.getId());
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(request, Collections.emptyList());

        assertThat(fromDb, equalTo(itemRequestDto));
    }

    @Test
    void shouldFindAll() {
        User user1 = User.builder().name("user1").email("user1@mail.com").build();
        User user2 = User.builder().name("user2").email("user2@mail.com").build();
        ItemRequest request = TestData.getNewItemRequest(user1);

        em.persist(user1);
        em.persist(user2);
        em.persist(request);
        List<ItemRequestDto> requestsForUser1 = requestService.getAll(user1.getId(), 0, 2);
        List<ItemRequestDto> requestsForUser2 = requestService.getAll(user2.getId(), 0, 1);
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(request, Collections.emptyList());

        assertThat(requestsForUser1, hasSize(0));
        assertThat(requestsForUser2, hasSize(1));
        assertThat(requestsForUser2, hasItem(itemRequestDto));
    }
}
