package ru.practicum.shareit;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.persistence.TypedQuery;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceTest {

    final EntityManager em;

    final UserService service;

    UserDto userDto;

    User user;

    Long failUserId = 10L;

    @BeforeEach
    void createUser() {
        userDto = new UserDto();
        userDto.setName("User Test1");
        userDto.setEmail("some@email.com");

        service.addUser(userDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        user = query
                .setParameter("email", userDto.getEmail())
                .getSingleResult();
    }

    @Test
    void saveUser() {
        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void updateUser() {
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setId(user.getId());
        userUpdateDto.setName("UpdatedName");
        userUpdateDto.setEmail("updated@email.com");

        service.updateUser(userUpdateDto);

        TypedQuery<User> queryUpdateUser = em.createQuery("Select u from User u where u.email = :email", User.class);
        User updateUser = queryUpdateUser
                .setParameter("email", userUpdateDto.getEmail())
                .getSingleResult();

        assertThat(updateUser.getId(), notNullValue());
        assertThat(updateUser.getEmail(), equalTo(userUpdateDto.getEmail()));
        assertThat(updateUser.getName(), equalTo(userUpdateDto.getName()));
    }

    @Test
    void deleteUser() {
        List<UserDto> allUsers = service.getAllUsers();

        assertThat(allUsers.size(), equalTo(1));

        service.deleteUser(user.getId());

        List<UserDto> newAllUsers = service.getAllUsers();

        assertThat(newAllUsers.size(), equalTo(0));
    }

    @Test
    void  isUserExists() {
        try {
            service.throwExceptionIfUserNotFound(failUserId);
        } catch (EntityNotFoundException ex) {
            assertThat(ex.getMessage(), equalTo("Пользователь ID = " + failUserId + " не найден."));
        }
    }
}
