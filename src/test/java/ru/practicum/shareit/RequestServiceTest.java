package ru.practicum.shareit;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestsService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestServiceTest {

    final EntityManager em;

    final UserService userService;

    final ItemRequestsService itemRequestsService;

    @Test
    @Rollback(false)
    void saveRequest() {
        UserDto userDto = new UserDto();
        userDto.setName("Owner Test1");
        userDto.setEmail("some@email.com");

        userService.addUser(userDto);

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .userId(1L)
                .created(LocalDateTime.now())
                .description("Request for text")
                .build();

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
