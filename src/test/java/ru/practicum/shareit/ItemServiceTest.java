package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingAddDto;
import ru.practicum.shareit.booking.dto.BookingAddMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentAddDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {

    private final EntityManager em;

    private final ItemService service;

    private final UserService userService;

    private final BookingRepository bookingRepository;

    @Test
    @Rollback(false)
    void saveItemAndComment() {
        UserDto userDto = new UserDto();
        userDto.setName("User Test1");
        userDto.setEmail("some@email.com");

        userService.addUser(userDto);

        UserDto ownerDto = new UserDto();
        ownerDto.setName("Owner Test1");
        ownerDto.setEmail("owner@email.com");

        userService.addUser(ownerDto);

        TypedQuery<User> queryUser = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = queryUser // ID = 1
                .setParameter("email", userDto.getEmail())
                .getSingleResult();

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("For Test");
        itemDto.setDescription("For Test");
        itemDto.setRequestId(0L);
        itemDto.setAvailable(true);
        itemDto.setOwner(2L);

        service.addItem(itemDto);

        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.name = :name", Item.class);
        Item item = query
                .setParameter("name", itemDto.getName())
                .getSingleResult();

        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(), equalTo(itemDto.getName()));
        assertThat(item.getDescription(), equalTo(itemDto.getDescription()));

        BookingAddDto bookingAddDto = new BookingAddDto();
        bookingAddDto.setItemId(1L);
        bookingAddDto.setUserId(1L);
        bookingAddDto.setStatus(BookingState.APPROVED);
        bookingAddDto.setStart(LocalDateTime.now().minusHours(2));
        bookingAddDto.setEnd(LocalDateTime.now().minusHours(1));

        BookingDto bookingDto = BookingAddMapper.getBookingDto(bookingAddDto, ItemMapper.getItemDto(item), user);

        bookingRepository.save(BookingMapper.getBooking(bookingDto));

        CommentAddDto commentAddDto = new CommentAddDto();
        commentAddDto.setItemId(1L);
        commentAddDto.setUserId(1L);
        commentAddDto.setText("Comment for test");
        commentAddDto.setCreated(LocalDateTime.now());

        service.addComment(commentAddDto);

        TypedQuery<Comment> queryComment = em.createQuery("Select i from Comment i where i.id = :id", Comment.class);
        Comment comment = queryComment
                .setParameter("id", 1L)
                .getSingleResult();

        assertThat(comment.getId(), notNullValue());
        assertThat(comment.getItem(), equalTo(item));
        assertThat(comment.getUser(), equalTo(user));
    }
}
