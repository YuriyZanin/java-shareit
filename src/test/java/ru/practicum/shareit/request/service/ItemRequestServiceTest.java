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
    private final ItemRequestService itemService;

    @Test
    void shouldSaveRequest() {
        User user = User.builder().name("test").email("test@mail.com").build();
        em.persist(user);

        ItemRequestCreationDto creationDto = new ItemRequestCreationDto(null, "description");
        ItemRequestDto saved = itemService.create(user.getId(), creationDto);

        ItemRequest request = em.createQuery("SELECT i FROM ItemRequest i WHERE i.id = :requestId", ItemRequest.class)
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

        List<ItemRequestDto> requests = itemService.getByUser(user.getId());
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(request, Collections.emptyList());
        assertThat(requests, hasSize(1));
        assertThat(requests, hasItem(itemRequestDto));
    }
}
