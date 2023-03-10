package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestCreationDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
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
        User user = User.builder().name("test").email("test@mail.com").build();
        em.persist(user);

        ItemRequestCreationDto creationDto = new ItemRequestCreationDto(null, "description");
        ItemRequestDto saved = requestService.create(user.getId(), creationDto);

        ItemRequest request = em.createQuery("SELECT i FROM ItemRequest i WHERE i.id = :requestId",
                        ItemRequest.class)
                .setParameter("requestId", saved.getId())
                .getSingleResult();
        assertThat(request.getId(), notNullValue());
        assertThat(request.getRequester(), equalTo(user));
        assertThat(request.getDescription(), equalTo(creationDto.getDescription()));
    }

    @Test
    void shouldFindByUser() {
        User user = User.builder().name("test").email("test@mail.com").build();
        em.persist(user);

        ItemRequest request = ItemRequest.builder().requester(user).created(LocalDateTime.now()).description("desc")
                .build();
        em.persist(request);

        List<ItemRequestDto> requests = requestService.getByUser(user.getId());
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(request, Collections.emptyList());
        assertThat(requests, hasSize(1));
        assertThat(requests, hasItem(itemRequestDto));
    }

    @Test
    void shouldFindById() {
        User user = User.builder().name("test").email("test@mail.com").build();
        em.persist(user);

        ItemRequest request = ItemRequest.builder().requester(user).created(LocalDateTime.now()).description("desc")
                .build();
        em.persist(request);

        ItemRequestDto fromDb = requestService.getById(user.getId(), request.getId());
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(request, Collections.emptyList());
        assertThat(fromDb, equalTo(itemRequestDto));
    }

    @Test
    void shouldFindAll() {
        User requester = User.builder().name("test").email("test@mail.com").build();
        em.persist(requester);

        User other = User.builder().name("name").email("other@mail.com").build();
        em.persist(other);

        ItemRequest request = ItemRequest
                .builder().requester(requester).created(LocalDateTime.now()).description("desc").build();
        em.persist(request);

        List<ItemRequestDto> all = requestService.getAll(requester.getId(), 0, 1);
        assertThat(all, hasSize(0));

        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(request, Collections.emptyList());
        all = requestService.getAll(other.getId(), 0, 1);
        assertThat(all, hasSize(1));
        assertThat(all, hasItem(itemRequestDto));
    }
}
