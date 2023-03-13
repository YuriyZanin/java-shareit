package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.CommentFullDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemFullDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.practicum.shareit.TestData.*;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {
    private final EntityManager em;
    private final ItemService itemService;

    private User owner;
    private Item item;

    @BeforeEach
    void setUp() {
        owner = getNewUser();
        item = getNewItem(owner);
    }

    @Test
    void shouldSaveItem() {
        ItemDto creationDto = ItemDto.builder().name("name").description("description").available(true).build();

        em.persist(owner);
        ItemFullDto created = itemService.create(owner.getId(), creationDto);
        Item fromDb = em.createQuery("SELECT i FROM Item i WHERE i.id = :id", Item.class)
                .setParameter("id", created.getId())
                .getSingleResult();

        assertThat(fromDb.getId(), equalTo(created.getId()));
        assertThat(fromDb.getOwner(), equalTo(owner));
        assertThat(fromDb.getName(), equalTo(creationDto.getName()));
        assertThat(fromDb.getDescription(), equalTo(creationDto.getDescription()));
        assertThat(fromDb.getAvailable(), equalTo(true));
    }

    @Test
    void shouldBeFailedIfItemRequestNotFound() {
        ItemDto creationDto = ItemDto.builder()
                .name("name").description("description").available(true).requestId(NOT_FOUND_ID).build();

        em.persist(owner);

        Assertions.assertThrows(NotFoundException.class, () -> itemService.create(owner.getId(), creationDto));
    }

    @Test
    void shouldBeFailedWhenUpdateOtherUser() {
        User notOwner = User.builder().email("user2@mail.com").name("user").build();
        ItemDto creationDto = ItemDto.builder()
                .name("name").description("description").available(true).requestId(NOT_FOUND_ID).build();

        em.persist(notOwner);
        em.persist(owner);
        em.persist(item);

        Assertions.assertThrows(NotFoundException.class,
                () -> itemService.update(notOwner.getId(), item.getId(), creationDto));
    }

    @Test
    void shouldUpdateItem() {
        ItemDto updateDto = ItemDto.builder()
                .name("updated name").available(false).description("updated description").build();

        em.persist(owner);
        em.persist(item);
        itemService.update(owner.getId(), item.getId(), updateDto);
        Item fromDb = em.createQuery("SELECT i FROM Item i WHERE i.id = :itemId", Item.class)
                .setParameter("itemId", item.getId())
                .getSingleResult();

        assertThat(fromDb.getId(), equalTo(item.getId()));
        assertThat(fromDb.getName(), equalTo(updateDto.getName()));
        assertThat(fromDb.getAvailable(), equalTo(updateDto.getAvailable()));
        assertThat(fromDb.getDescription(), equalTo(updateDto.getDescription()));
    }

    @Test
    void shouldGetById() {
        em.persist(owner);
        em.persist(item);
        ItemFullDto fromDb = itemService.get(owner.getId(), item.getId());
        ItemFullDto fullDto = ItemMapper.toItemFullDto(item, null, null);

        assertThat(fromDb, equalTo(fullDto));
    }

    @Test
    void shouldGetAllByUser() {
        em.persist(owner);
        em.persist(item);
        List<ItemFullDto> allByUser = itemService.getAllByUser(owner.getId(), 0, 1);
        ItemFullDto itemFullDto = ItemMapper.toItemFullDto(item, null, null);

        assertThat(allByUser, hasSize(1));
        assertThat(allByUser, hasItem(itemFullDto));
    }

    @Test
    void shouldGetByText() {
        em.persist(owner);
        em.persist(item);
        List<ItemFullDto> all = itemService.getByText(owner.getId(), "description", 0, 1);
        ItemFullDto itemFullDto = ItemMapper.toItemFullDto(item, null, null);

        assertThat(all, hasSize(1));
        assertThat(all, hasItem(itemFullDto));
    }

    @Test
    void shouldAddComment() {
        Booking booking = Booking.builder()
                .booker(owner).item(item).start(LocalDateTime.now().minusHours(1)).end(LocalDateTime.now())
                .status(Status.APPROVED)
                .build();

        em.persist(owner);
        em.persist(item);
        em.persist(booking);
        CommentCreationDto commentCreationDto = new CommentCreationDto(null, "test comment");
        CommentFullDto commentFullDto = itemService.addComment(owner.getId(), item.getId(), commentCreationDto);

        assertThat(commentFullDto.getId(), notNullValue());
        assertThat(commentFullDto.getText(), equalTo(commentCreationDto.getText()));
        assertThat(commentFullDto.getAuthorName(), equalTo(owner.getName()));
    }

    @Test
    void shouldDeleteById() {
        em.persist(owner);
        em.persist(item);
        itemService.delete(owner.getId(), item.getId());
        List<Item> items = em.createQuery("SELECT i FROM Item i WHERE i.id = :itemId", Item.class)
                .setParameter("itemId", item.getId())
                .getResultList();

        assertThat(items, hasSize(0));
    }
}
