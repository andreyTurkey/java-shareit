package ru.practicum.shareit;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingAddDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingGetOwnerDto;
import ru.practicum.shareit.booking.dto.BookingMapperGetOwnerDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingServiceTest {

    final ItemService itemService;

    final EntityManager em;

    final BookingService bookingService;

    final UserService userService;

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
    void saveBooking() {
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

        BookingAddDto bookingAddDto = new BookingAddDto();
        bookingAddDto.setItemId(item.getId());
        bookingAddDto.setUserId(user.getId());
        bookingAddDto.setStart(LocalDateTime.now().plusMinutes(10));
        bookingAddDto.setEnd(LocalDateTime.now().plusHours(1));

        bookingService.addBooking(bookingAddDto);

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.start = :start", Booking.class);
        Booking booking = query
                .setParameter("start", bookingAddDto.getStart())
                .getSingleResult();

        MatcherAssert.assertThat(booking.getId(), Matchers.notNullValue());
        MatcherAssert.assertThat(bookingAddDto.getStart(), Matchers.equalTo(booking.getStart()));
        MatcherAssert.assertThat(bookingAddDto.getEnd(), Matchers.equalTo(booking.getEnd()));

        BookingAddDto bookingAddDtoFailOwner = new BookingAddDto();
        bookingAddDtoFailOwner.setItemId(item.getId());
        bookingAddDtoFailOwner.setUserId(owner.getId());
        bookingAddDtoFailOwner.setStart(LocalDateTime.now().plusMinutes(10));
        bookingAddDtoFailOwner.setEnd(LocalDateTime.now().plusHours(1));

        try {
            bookingService.addBooking(bookingAddDtoFailOwner);
        } catch (EntityNotFoundException thrown) {
            MatcherAssert.assertThat(thrown.getMessage(), Matchers.equalTo("Владелец вещи не может создавать бронирование"));
        }

        BookingAddDto bookingAddDtoFailDate = new BookingAddDto();
        bookingAddDtoFailDate.setItemId(item.getId());
        bookingAddDtoFailDate.setUserId(user.getId());
        bookingAddDtoFailDate.setStart(LocalDateTime.now().minusHours(10));
        bookingAddDtoFailDate.setEnd(LocalDateTime.now().plusHours(1));

        try {
            bookingService.addBooking(bookingAddDtoFailDate);
        } catch (NotAvailableException thrown) {
            MatcherAssert.assertThat(thrown.getMessage(), Matchers.equalTo("Проверьте даты бронирования"));
        }
    }

    @Test
    void updateStatus() {
        TypedQuery<User> queryUser1 = em.createQuery("Select u from User u where u.email = :email", User.class);
        owner = queryUser1
                .setParameter("email", ownerDto.getEmail())
                .getSingleResult();

        TypedQuery<Item> queryItem = em.createQuery("Select i from Item i where i.name = :name", Item.class);
        item = queryItem
                .setParameter("name", itemDto.getName())
                .getSingleResult();

        BookingAddDto bookingAddDto = new BookingAddDto();
        bookingAddDto.setItemId(item.getId());
        bookingAddDto.setUserId(user.getId());
        bookingAddDto.setStart(LocalDateTime.now().plusMinutes(10));
        bookingAddDto.setEnd(LocalDateTime.now().plusHours(1));

        bookingService.addBooking(bookingAddDto);

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.start = :start", Booking.class);
        Booking booking = query
                .setParameter("start", bookingAddDto.getStart())
                .getSingleResult();

        TypedQuery<User> queryUser = em.createQuery("Select u from User u where u.email = :email", User.class);
        user = queryUser
                .setParameter("email", userDto.getEmail())
                .getSingleResult();

        TypedQuery<User> queryOwner = em.createQuery("Select u from User u where u.email = :email", User.class);
        owner = queryOwner
                .setParameter("email", owner.getEmail())
                .getSingleResult();

        bookingService.updateStatus(owner.getId(), booking.getId(), true);

        TypedQuery<Booking> queryStatus = em.createQuery("Select b from Booking b where b.start = :start", Booking.class);
        Booking bookingNewStatus = queryStatus
                .setParameter("start", bookingAddDto.getStart())
                .getSingleResult();

        MatcherAssert.assertThat(booking.getStatus(), Matchers.equalTo(bookingNewStatus.getStatus()));

        try {
            bookingService.updateStatus(failUser, booking.getId(), true);
        } catch (EntityNotFoundException thrown) {
            MatcherAssert.assertThat(thrown.getMessage(), Matchers.equalTo("Проверьте ID = " + failUser + " пользователя"));
        }

        try {
            bookingService.updateStatus(owner.getId(), booking.getId(), false);
        } catch (NotAvailableException thrown) {
            MatcherAssert.assertThat(thrown.getMessage(), Matchers.equalTo("Бронирование уже подтверждено"));
        }
    }

    @Test
    void getBookingById() {
        TypedQuery<Item> queryItem = em.createQuery("Select i from Item i where i.name = :name", Item.class);
        item = queryItem
                .setParameter("name", itemDto.getName())
                .getSingleResult();

        BookingAddDto bookingAddDto = new BookingAddDto();
        bookingAddDto.setItemId(item.getId());
        bookingAddDto.setUserId(user.getId());
        bookingAddDto.setStart(LocalDateTime.now().plusMinutes(10));
        bookingAddDto.setEnd(LocalDateTime.now().plusHours(1));

        bookingService.addBooking(bookingAddDto);

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.start = :start", Booking.class);
        Booking booking = query
                .setParameter("start", bookingAddDto.getStart())
                .getSingleResult();

        BookingDto bookingFromDB = bookingService.getBookingById(booking.getId(), user.getId(), ownerDto.getId());
        MatcherAssert.assertThat(bookingAddDto.getStart(), Matchers.equalTo(bookingFromDB.getStart()));

        try {
            bookingService.getBookingById(booking.getId(), failUser, ownerDto.getId());
        } catch (EntityNotFoundException thrown) {
            MatcherAssert.assertThat(thrown.getMessage(), Matchers.equalTo("Проверьте ID = " + failUser + " пользователя"));
        }
    }

    @Test
    void getAllBookingsByUserId() {
        TypedQuery<Item> queryItem = em.createQuery("Select i from Item i where i.name = :name", Item.class);
        item = queryItem
                .setParameter("name", itemDto.getName())
                .getSingleResult();

        BookingAddDto bookingAddDto = new BookingAddDto();
        bookingAddDto.setItemId(item.getId());
        bookingAddDto.setUserId(user.getId());
        bookingAddDto.setStart(LocalDateTime.now().plusMinutes(10));
        bookingAddDto.setEnd(LocalDateTime.now().plusHours(1));

        bookingService.addBooking(bookingAddDto);

        List<BookingDto> bookingsByUserId = bookingService.getAllBookingsByUserId(
                user.getId(), "ALL", null, null);

        MatcherAssert.assertThat(bookingsByUserId.size(), Matchers.equalTo(1));

        List<BookingDto> bookingsByUserIdWithPage = bookingService.getAllBookingsByUserId(
                user.getId(), "ALL", 0, 20);

        MatcherAssert.assertThat(bookingsByUserIdWithPage.size(), Matchers.equalTo(1));

        List<BookingDto> bookingsByUserIdWithPageFuture = bookingService.getAllBookingsByUserId(
                user.getId(), "FUTURE", null, null);

        MatcherAssert.assertThat(bookingsByUserIdWithPageFuture.size(), Matchers.equalTo(1));

        List<BookingDto> bookingsByUserIdWithPagePast = bookingService.getAllBookingsByUserId(
                user.getId(), "PAST", null, null);

        MatcherAssert.assertThat(bookingsByUserIdWithPagePast.size(), Matchers.equalTo(0));

        List<BookingDto> bookingsByUserIdWithPageCurrent = bookingService.getAllBookingsByUserId(
                user.getId(), "CURRENT", null, null);

        MatcherAssert.assertThat(bookingsByUserIdWithPageCurrent.size(), Matchers.equalTo(0));

        List<BookingDto> bookingsByUserIdWithPageWaiting = bookingService.getAllBookingsByUserId(
                user.getId(), "WAITING", null, null);

        MatcherAssert.assertThat(bookingsByUserIdWithPageWaiting.size(), Matchers.equalTo(1));

        List<BookingDto> bookingsByUserIdWithPageRejected = bookingService.getAllBookingsByUserId(
                user.getId(), "REJECTED", null, null);

        MatcherAssert.assertThat(bookingsByUserIdWithPageRejected.size(), Matchers.equalTo(0));

        try {
            List<BookingDto> bookingsByUserIdUnknownStasus = bookingService.getAllBookingsByUserId(
                    user.getId(), "UNKNOWN", null, null);
        } catch (NotAvailableException thrown) {
            MatcherAssert.assertThat(thrown.getMessage(), Matchers.equalTo("Unknown state: UNSUPPORTED_STATUS"));
        }
    }

    @Test
    void getAllBookingsByOwnerId() {
        TypedQuery<Item> queryItem = em.createQuery("Select i from Item i where i.name = :name", Item.class);
        item = queryItem
                .setParameter("name", itemDto.getName())
                .getSingleResult();

        BookingAddDto bookingAddDto = new BookingAddDto();
        bookingAddDto.setItemId(item.getId());
        bookingAddDto.setUserId(user.getId());
        bookingAddDto.setStart(LocalDateTime.now().plusMinutes(10));
        bookingAddDto.setEnd(LocalDateTime.now().plusHours(1));

        bookingService.addBooking(bookingAddDto);

        List<BookingDto> allBookingsByOwnerId = bookingService.getAllBookingsByOwnerId(
                owner.getId(), "ALL", 0, 20);

        MatcherAssert.assertThat(allBookingsByOwnerId.size(), Matchers.equalTo(1));

        List<BookingDto> bookingsByOwnerIdWithoutPage = bookingService.getAllBookingsByOwnerId(
                owner.getId(), "ALL", null, null);

        MatcherAssert.assertThat(bookingsByOwnerIdWithoutPage.size(), Matchers.equalTo(1));

        List<BookingDto> bookingsByOwnerIdWithPageFuture = bookingService.getAllBookingsByOwnerId(
                owner.getId(), "FUTURE", null, null);

        MatcherAssert.assertThat(bookingsByOwnerIdWithPageFuture.size(), Matchers.equalTo(1));

        List<BookingDto> bookingsByOwnerIdWithPagePast = bookingService.getAllBookingsByOwnerId(
                owner.getId(), "PAST", null, null);

        MatcherAssert.assertThat(bookingsByOwnerIdWithPagePast.size(), Matchers.equalTo(0));

        List<BookingDto> bookingsByOwnerIdWithPageCurrent = bookingService.getAllBookingsByOwnerId(
                owner.getId(), "CURRENT", null, null);

        MatcherAssert.assertThat(bookingsByOwnerIdWithPageCurrent.size(), Matchers.equalTo(0));

        List<BookingDto> bookingsByOwnerIdWithPageWaiting = bookingService.getAllBookingsByOwnerId(
                owner.getId(), "WAITING", null, null);

        MatcherAssert.assertThat(bookingsByOwnerIdWithPageWaiting.size(), Matchers.equalTo(1));

        List<BookingDto> bookingsByOwnerIdWithPageRejected = bookingService.getAllBookingsByOwnerId(
                owner.getId(), "REJECTED", null, null);

        MatcherAssert.assertThat(bookingsByOwnerIdWithPageRejected.size(), Matchers.equalTo(0));

        try {
            List<BookingDto> bookingsByOwnerIdUnknownStasus = bookingService.getAllBookingsByOwnerId(
                    owner.getId(), "UNKNOWN", null, null);
        } catch (NotAvailableException thrown) {
            MatcherAssert.assertThat(thrown.getMessage(), Matchers.equalTo("Unknown state: UNSUPPORTED_STATUS"));
        }
    }

    @Test
    void getBookingGetOwnerDto() {
        TypedQuery<Item> queryItem = em.createQuery("Select i from Item i where i.name = :name", Item.class);
        item = queryItem
                .setParameter("name", itemDto.getName())
                .getSingleResult();

        BookingAddDto bookingAddDto = new BookingAddDto();
        bookingAddDto.setItemId(item.getId());
        bookingAddDto.setUserId(user.getId());
        bookingAddDto.setStart(LocalDateTime.now().plusMinutes(10));
        bookingAddDto.setEnd(LocalDateTime.now().plusHours(1));

        bookingService.addBooking(bookingAddDto);

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.start = :start", Booking.class);
        Booking booking = query
                .setParameter("start", bookingAddDto.getStart())
                .getSingleResult();

        BookingGetOwnerDto bookingGetOwnerDto = BookingMapperGetOwnerDto.getBookingGetOwnerDto(booking);
        MatcherAssert.assertThat(bookingGetOwnerDto.getId(), Matchers.equalTo(booking.getId()));
    }
}