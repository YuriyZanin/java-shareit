package ru.practicum.shareit.request.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.AbstractRepositoryTest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RequestRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Test
    void shouldSaveRequest() {
        User requester = User.builder().name("test").email("test@mail.com").build();
        entityManager.persist(requester);

        LocalDateTime now = LocalDateTime.now();
        ItemRequest request = itemRequestRepository.save(
                ItemRequest.builder().description("test").requester(requester).created(now).build());

        assertThat(request.getId(), notNullValue());
        assertThat(request.getRequester(), equalTo(requester));
        assertThat(request.getDescription(), equalTo("test"));
        assertThat(request.getCreated(), equalTo(now));
    }

    @Test
    void shouldFindByRequester() {
        User requester = User.builder().name("test").email("test@mail.com").build();
        entityManager.persist(requester);

        LocalDateTime now = LocalDateTime.now();
        ItemRequest test = ItemRequest.builder().description("test").requester(requester).created(now).build();
        entityManager.persist(test);

        Sort sort = Sort.by("created");
        List<ItemRequest> requests = itemRequestRepository.findByRequesterId(requester.getId(), sort);

        assertThat(requests, hasSize(1));
        assertThat(requests, hasItem(test));

        PageRequest pageable = PageRequest.of(0, 1);
        Page<ItemRequest> page = itemRequestRepository.findByRequesterIdNot(pageable, requester.getId());

        assertThat(page, notNullValue());
        assertThat(page.getContent(), hasSize(0));
    }
}
