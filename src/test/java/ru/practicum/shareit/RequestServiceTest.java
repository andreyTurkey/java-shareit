package ru.practicum.shareit;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestsService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.time.LocalDateTime;

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

    @Test
    void saveRequest() {
        UserDto userDto = new UserDto();
        userDto.setName("Owner Test3");
        userDto.setEmail("some3@email.com");

        userService.addUser(userDto);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setUserId(2L);
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setDescription("Request for text");

        itemRequestsService.addRequest(itemRequestDto);

        TypedQuery<ItemRequest> query = em.createQuery("Select i from ItemRequest i where i.id = :id", ItemRequest.class);
        ItemRequest itemRequest = query
                .setParameter("id", 1L)
                .getSingleResult();

        assertThat(itemRequest.getId(), notNullValue());
        assertThat(itemRequestDto.getUserId(), equalTo(itemRequest.getUserId()));
        assertThat(itemRequestDto.getDescription(), equalTo(itemRequest.getDescription()));
    }
}
