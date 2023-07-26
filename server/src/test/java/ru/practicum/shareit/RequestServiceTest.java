package ru.practicum.shareit;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestsService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestPublicDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestServiceTest {

    final EntityManager em;

    final UserService userService;

    final ItemRequestsService itemRequestsService;

    final ItemService itemService;

    UserDto userDto;

    UserDto userDto1;

    UserDto userDto2;

    ItemRequestDto itemRequestDto;

    @BeforeEach
    void createUsers() {
        userDto = new UserDto();
        userDto.setName("Owner Test3");
        userDto.setEmail("some3@email.com");

        userService.addUser(userDto);

        userDto1 = new UserDto();
        userDto1.setName("User Test3");
        userDto1.setEmail("some4@email.com");

        userService.addUser(userDto1);

        userDto2 = new UserDto();
        userDto2.setName("User2 Test3");
        userDto2.setEmail("some5@email.com");

        userService.addUser(userDto2);
    }

    @Test
    void saveRequest() {
        TypedQuery<User> queryUser = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = queryUser
                .setParameter("email", userDto.getEmail())
                .getSingleResult();

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setUserId(user.getId());
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setDescription("Request for text");

        itemRequestsService.addRequest(itemRequestDto);

        TypedQuery<ItemRequest> query = em.createQuery("Select i from ItemRequest i where i.description = :description",
                ItemRequest.class);
        ItemRequest itemRequest = query
                .setParameter("description", itemRequestDto.getDescription())
                .getSingleResult();

        assertThat(itemRequest.getId(), notNullValue());
        assertThat(itemRequestDto.getUserId(), equalTo(itemRequest.getUserId()));
        assertThat(itemRequestDto.getDescription(), equalTo(itemRequest.getDescription()));
    }

    @Test
    void getAllRequestByUserId() {
        TypedQuery<User> queryUser1 = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = queryUser1
                .setParameter("email", userDto1.getEmail())
                .getSingleResult();

        TypedQuery<User> queryUser2 = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user2 = queryUser2
                .setParameter("email", userDto2.getEmail())
                .getSingleResult();

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setUserId(user.getId());
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setDescription("Request for text");

        itemRequestsService.addRequest(itemRequestDto);

        ItemRequestDto itemRequestDto2 = new ItemRequestDto();
        itemRequestDto2.setUserId(user2.getId());
        itemRequestDto2.setCreated(LocalDateTime.now());
        itemRequestDto2.setDescription("Request for text1");

        itemRequestsService.addRequest(itemRequestDto2);

        TypedQuery<ItemRequest> query = em.createQuery(
                "Select i from ItemRequest i where i.description = :description", ItemRequest.class);
        ItemRequest itemRequest = query
                .setParameter("description", itemRequestDto.getDescription())
                .getSingleResult();

        TypedQuery<ItemRequest> queryRequest1 = em.createQuery(
                "Select i from ItemRequest i where i.description = :description", ItemRequest.class);
        ItemRequest itemRequest1 = queryRequest1
                .setParameter("description", itemRequestDto2.getDescription())
                .getSingleResult();

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("For Test");
        itemDto.setDescription("For Test");
        itemDto.setRequestId(itemRequest1.getId());
        itemDto.setAvailable(true);
        itemDto.setOwner(user2.getId());

        itemService.addItem(itemDto);

        ItemDto itemDto1 = new ItemDto();
        itemDto1.setId(1L);
        itemDto1.setName("For Test");
        itemDto1.setDescription("For Test");
        itemDto1.setRequestId(itemRequest.getId());
        itemDto1.setAvailable(true);
        itemDto1.setOwner(user2.getId());

        itemService.addItem(itemDto1);

        List<ItemRequestPublicDto> getAllRequestByUserId = itemRequestsService.getAllRequestByUserId(user.getId());

        assertThat(getAllRequestByUserId.size(), equalTo(1));
        assertThat(itemRequest.getId(), equalTo(getAllRequestByUserId.get(0).getId()));
        assertThat(true, equalTo(itemRequestsService.requestExist(itemRequest.getId())));

        try {
            itemRequestsService.requestExist(5L);
        } catch (EntityNotFoundException thrown) {
            assertThat(thrown.getMessage(), equalTo("Запрос не найден"));
        }
    }

    @Test
    void getRequestById() {
        TypedQuery<User> queryUser1 = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = queryUser1
                .setParameter("email", userDto1.getEmail())
                .getSingleResult();

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setUserId(user.getId());
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setDescription("Request for text");

        itemRequestsService.addRequest(itemRequestDto);

        TypedQuery<ItemRequest> query = em.createQuery(
                "Select i from ItemRequest i where i.description = :description", ItemRequest.class);
        ItemRequest itemRequest = query
                .setParameter("description", itemRequestDto.getDescription())
                .getSingleResult();

        ItemRequestPublicDto itemRequestPublicDto = itemRequestsService.getRequestById(itemRequest.getId(), user.getId());

        assertThat(itemRequestDto.getDescription(), equalTo(itemRequestPublicDto.getDescription()));
    }

    @Test
    void getAllRequestByPageable() throws NotAvailableException {
        TypedQuery<User> queryUser1 = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = queryUser1
                .setParameter("email", userDto1.getEmail())
                .getSingleResult();

        TypedQuery<User> queryUser2 = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user1 = queryUser2
                .setParameter("email", userDto1.getEmail())
                .getSingleResult();

        TypedQuery<User> queryUser3 = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user2 = queryUser3
                .setParameter("email", userDto2.getEmail())
                .getSingleResult();

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setUserId(user.getId());
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setDescription("Request for test");

        itemRequestsService.addRequest(itemRequestDto);

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("For Test");
        itemDto.setDescription("For Test");
        itemDto.setRequestId(0L);
        itemDto.setAvailable(true);
        itemDto.setOwner(user1.getId());

        itemService.addItem(itemDto);

        List<ItemRequestPublicDto> getAllRequestByPageable
                = itemRequestsService.getAllRequestByPageable(0, 20, user2.getId());

        assertThat(getAllRequestByPageable.size(), equalTo(1));

        List<ItemRequestPublicDto> getAllRequestByPageablePageNull
                = itemRequestsService.getAllRequestByPageable(null, null, user2.getId());

        assertThat(getAllRequestByPageablePageNull.size(), equalTo(1));
    }
}