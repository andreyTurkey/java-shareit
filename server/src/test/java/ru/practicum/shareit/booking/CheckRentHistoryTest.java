package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentAddDto;
import ru.practicum.shareit.item.dto.CommentAddMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CheckRentHistoryTest {

    final ItemService itemService;

    final EntityManager em;

    final BookingRepository bookingRepository;

    final UserService userService;

    final CheckRentHistory checkRentHistory;

    UserDto userDto;

    UserDto ownerDto;

    User user;

    User owner;

    ItemDto itemDto;

    Item item;

    Long failUser = 10L;

    @BeforeEach
    void createUsers() {
        userDto = new UserDto();
        userDto.setName("Owner Test1");
        userDto.setEmail("owner@email.com");

        userService.addUser(userDto);

        ownerDto = new UserDto();
        ownerDto.setName("User Test1");
        ownerDto.setEmail("user@email.com");

        userService.addUser(ownerDto);

        TypedQuery<User> queryUser = em.createQuery("Select u from User u where u.email = :email", User.class);
        user = queryUser
                .setParameter("email", userDto.getEmail())
                .getSingleResult();

        TypedQuery<User> queryUser1 = em.createQuery("Select u from User u where u.email = :email", User.class);
        owner = queryUser1
                .setParameter("email", ownerDto.getEmail())
                .getSingleResult();

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("For Test");
        itemDto.setDescription("For Test");
        itemDto.setRequestId(0L);
        itemDto.setAvailable(true);
        itemDto.setOwner(owner.getId());

        itemService.addItem(itemDto);
    }

    @Test
    void isUserTookItem() {
        TypedQuery<User> queryUser = em.createQuery("Select u from User u where u.email = :email", User.class);
        user = queryUser
                .setParameter("email", userDto.getEmail())
                .getSingleResult();

        TypedQuery<User> queryUser1 = em.createQuery("Select u from User u where u.email = :email", User.class);
        owner = queryUser1
                .setParameter("email", ownerDto.getEmail())
                .getSingleResult();

        TypedQuery<Item> queryItem = em.createQuery("Select i from Item i where i.name = :name", Item.class);
        item = queryItem
                .setParameter("name", itemDto.getName())
                .getSingleResult();

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setUser(user);
        booking.setStatus(BookingState.APPROVED);
        booking.setStart(LocalDateTime.now().minusDays(3));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        bookingRepository.save(booking);

        CommentAddDto commentAddDto = new CommentAddDto();
        commentAddDto.setItemId(item.getId());
        commentAddDto.setUserId(user.getId());
        commentAddDto.setText("Comment for test");
        commentAddDto.setCreated(LocalDateTime.now());

        itemService.addComment(commentAddDto);

        TypedQuery<Comment> queryComment = em.createQuery("Select i from Comment i where i.text = :text", Comment.class);
        Comment comment = queryComment
                .setParameter("text", commentAddDto.getText())
                .getSingleResult();

        checkRentHistory.isUserTookItem(CommentAddMapper.getCommentAddDto(comment));

        assertThat(checkRentHistory.isUserTookItem(CommentAddMapper.getCommentAddDto(comment)),
                equalTo(true));

        CommentAddDto commentAddDtoFailUser = new CommentAddDto();
        commentAddDtoFailUser.setItemId(item.getId());
        commentAddDtoFailUser.setUserId(owner.getId());
        commentAddDtoFailUser.setText("Comment for test");
        commentAddDtoFailUser.setCreated(LocalDateTime.now());

        try {
            itemService.addComment(commentAddDtoFailUser);
        } catch (NotAvailableException ex) {
            assertThat(ex.getMessage(),
                    equalTo("Пользователь не брал вещь в аренду."));
        }
    }
}
