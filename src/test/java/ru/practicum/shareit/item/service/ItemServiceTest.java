package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
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
import static ru.practicum.shareit.TestData.getNewItem;
import static ru.practicum.shareit.TestData.getNewUser;

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
        em.persist(owner);

        ItemDto itemDto = ItemDto.builder().name("name").description("description").available(true).build();
        ItemFullDto itemFullDto = itemService.create(owner.getId(), itemDto);

        Item item = em.createQuery("SELECT i FROM Item i WHERE i.id = :id", Item.class)
                .setParameter("id", itemFullDto.getId())
                .getSingleResult();

        assertThat(item.getId(), equalTo(itemFullDto.getId()));
        assertThat(item.getOwner(), equalTo(owner));
        assertThat(item.getName(), equalTo(itemDto.getName()));
        assertThat(item.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(item.getAvailable(), equalTo(true));
    }

    @Test
    void shouldUpdateItem() {
        em.persist(owner);
        em.persist(item);

        ItemDto creationDto = ItemDto.builder()
                .name("updated name").available(false).description("updated description").build();

        itemService.update(owner.getId(), item.getId(), creationDto);

        Item fromDb = em.createQuery("SELECT i FROM Item i WHERE i.id = :itemId", Item.class)
                .setParameter("itemId", item.getId())
                .getSingleResult();

        assertThat(fromDb.getId(), equalTo(item.getId()));
        assertThat(fromDb.getName(), equalTo(creationDto.getName()));
        assertThat(fromDb.getAvailable(), equalTo(creationDto.getAvailable()));
        assertThat(fromDb.getDescription(), equalTo(creationDto.getDescription()));
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
        em.persist(owner);
        em.persist(item);

        Booking booking = Booking.builder()
                .booker(owner).item(item).start(LocalDateTime.now().minusHours(1)).end(LocalDateTime.now())
                .status(Status.APPROVED)
                .build();
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
