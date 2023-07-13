package ru.practicum.shareit;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingAddDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
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
public class BookingServiceTest {

    final ItemService itemService;

    final EntityManager em;

    final BookingService bookingService;

    final UserService userService;

    @Test
    @Rollback(false)
    void saveBooking() {
        UserDto userDto = new UserDto();
        userDto.setName("Owner Test1");
        userDto.setEmail("some@email.com");

        userService.addUser(userDto);

        UserDto ownerDto = new UserDto();
        ownerDto.setName("User Test1");
        ownerDto.setEmail("owner@email.com");

        userService.addUser(ownerDto);

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("For Test");
        itemDto.setDescription("For Test");
        itemDto.setRequestId(0L);
        itemDto.setAvailable(true);
        itemDto.setOwner(2L);

        itemService.addItem(itemDto);

        BookingAddDto bookingAddDto = new BookingAddDto();
        bookingAddDto.setItemId(1L);
        bookingAddDto.setUserId(1L);
        bookingAddDto.setStart(LocalDateTime.now().plusMinutes(10));
        bookingAddDto.setEnd(LocalDateTime.now().plusHours(1));

        bookingService.addBooking(bookingAddDto);

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking booking = query
                .setParameter("id", 1L)
                .getSingleResult();

        assertThat(booking.getId(), notNullValue());
        assertThat(bookingAddDto.getStart(), equalTo(booking.getStart()));
        assertThat(bookingAddDto.getEnd(), equalTo(booking.getEnd()));
    }
}
