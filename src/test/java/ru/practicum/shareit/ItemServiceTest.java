package ru.practicum.shareit;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemServiceTest {

    final EntityManager em;

    final ItemService service;

    final UserService userService;

    final BookingRepository bookingRepository;

    final BookingService bookingService;

    UserDto userDto;

    UserDto ownerDto;

    User user;

    User owner;

    Item item;

    ItemDto itemDto;

    Long itemFailId = 10L;

    @BeforeEach
    void createUser() {
        userDto = new UserDto();
        userDto.setName("User Test2");
        userDto.setEmail("some2@email.com");

        userService.addUser(userDto);

        ownerDto = new UserDto();
        ownerDto.setName("Owner Test2");
        ownerDto.setEmail("owner2@email.com");

        userService.addUser(ownerDto);

        TypedQuery<User> queryUser1 = em.createQuery("Select u from User u where u.email = :email", User.class);
        user = queryUser1
                .setParameter("email", userDto.getEmail())
                .getSingleResult();

        TypedQuery<User> queryUser2 = em.createQuery("Select u from User u where u.email = :email", User.class);
        owner = queryUser2
                .setParameter("email", ownerDto.getEmail())
                .getSingleResult();

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("For Test");
        itemDto.setDescription("For Test");
        itemDto.setRequestId(0L);
        itemDto.setAvailable(true);
        itemDto.setOwner(owner.getId());

        service.addItem(itemDto);

        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.name = :name", Item.class);
        item = query
                .setParameter("name", itemDto.getName())
                .getSingleResult();
    }

    @Test
    void saveItemAndComment() {
        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(), equalTo(itemDto.getName()));
        assertThat(item.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(item.getOwner(), equalTo(itemDto.getOwner()));

        BookingAddDto bookingAddDto = new BookingAddDto();
        bookingAddDto.setItemId(1L);
        bookingAddDto.setUserId(user.getId());
        bookingAddDto.setStatus(BookingState.APPROVED);
        bookingAddDto.setStart(LocalDateTime.now().minusHours(2));
        bookingAddDto.setEnd(LocalDateTime.now().minusHours(1));

        BookingDto bookingDto = BookingAddMapper.getBookingDto(bookingAddDto, ItemMapper.getItemDto(item), user);

        bookingRepository.save(BookingMapper.getBooking(bookingDto));

        CommentAddDto commentAddDto = new CommentAddDto();
        commentAddDto.setItemId(item.getId());
        commentAddDto.setUserId(user.getId());
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

    @Test
    void findByIdAndOwner() {
        ItemPublicDto itemFromDb = service.findByIdAndOwner(item.getId(), user.getId());

        assertThat(itemFromDb.getName(), equalTo(item.getName()));

        try {
            service.findByIdAndOwner(itemFailId, user.getId());
        } catch (EntityNotFoundException ex) {
            assertThat(ex.getMessage(), equalTo("Вещь не найдена"));
        }
    }

    @Test
    void updateItem() {
        ItemUpdateDto itemUpdateDto =  new ItemUpdateDto();
        itemUpdateDto.setDescription("Updated Item");


        ItemDto updatedItem = service.updateItem(itemUpdateDto, owner.getId(), item.getId());

        assertThat(updatedItem.getDescription(), equalTo(itemUpdateDto.getDescription()));
    }

    public List<Booking> getAllBookingByOwnerId(Long ownerId) {
        List<Booking> bookings = em.createQuery(
                        "SELECT b from Booking b join Item i on b.item.id = i.id WHERE i.owner =:id ORDER BY b.id DESC")
                .setParameter("id", ownerId)
                .getResultList();
        return bookings;
    }

    @Test
    void getAllBookingByOwnerId( ){
        BookingAddDto bookingAddDto = new BookingAddDto();
        bookingAddDto.setItemId(item.getId());
        bookingAddDto.setUserId(user.getId());
        bookingAddDto.setStart(LocalDateTime.now().plusMinutes(10));
        bookingAddDto.setEnd(LocalDateTime.now().plusHours(1));

        bookingService.addBooking(bookingAddDto);

        List<Booking> allBookingByOwnerId = service.getAllBookingByOwnerId(owner.getId());

        assertThat(allBookingByOwnerId.size(), equalTo(1));
    }

    @Test
    void findAllByUser() {
        List<ItemPublicDto> items = service.findAllByUser(owner.getId());

        assertThat(items.size(), equalTo(1));
    }
}
