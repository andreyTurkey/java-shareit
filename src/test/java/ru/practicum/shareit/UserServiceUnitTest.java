package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    UserRepository mockUserRepository;

    @Test
    void testAddUserWithMockito() {
        UserService userService = new UserService(mockUserRepository);

        User user = new User();
        user.setId(1L);
        user.setName("UserForTest");
        user.setEmail("mail@mail.ru");

        Mockito
                .when(mockUserRepository.save(user)).
                thenReturn(user);

        Assertions.assertEquals(UserMapper.getUserDto(user), userService.addUser(UserMapper.getUserDto(user)));
    }

    @Test
    void testUpdateUserWithMockito() {
        UserService userService = new UserService(mockUserRepository);

        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setId(1L);
        userUpdateDto.setName("UserForTest");
        userUpdateDto.setEmail("UserUpdateTestEmail");


        User user = new User();
        user.setId(1L);
        user.setName("UserForTest");
        user.setEmail("mail@mail.ru");

        Mockito
                .when(mockUserRepository.getReferenceById(Mockito.anyLong())).
                thenReturn(user);

        Mockito
                .when(mockUserRepository.save(user)).
                thenReturn(user);

        Assertions.assertEquals(UserMapper.getUserDtoFromUpdateDto(userUpdateDto), userService.updateUser(userUpdateDto));
    }

    @Test
    void testDeleteUserWithMockito() {
        UserService userService = new UserService(mockUserRepository);

        User user = new User();
        user.setId(1L);
        user.setName("UserForTest");
        user.setEmail("mail@mail.ru");

        Assertions.assertEquals(true, userService.deleteUser(user.getId()));
    }

    @Test
    void testGetAllUsersWithMockito() {
        UserService userService = new UserService(mockUserRepository);

        User user = new User();
        user.setId(1L);
        user.setName("UserForTest");
        user.setEmail("mail@mail.ru");

        Mockito
                .when(mockUserRepository.findAll()).
                thenReturn(List.of(user));

        Assertions.assertEquals(List.of(user).size(),
                userService.getAllUsers().size());

        Assertions.assertEquals(List.of(user).stream().map(UserMapper::getUserDto).collect(Collectors.toList()),
                userService.getAllUsers());
    }

    @Test
    void testExistUserWithMockito() {
        UserService userService = new UserService(mockUserRepository);

        User user = new User();
        user.setId(1L);
        user.setName("UserForTest");
        user.setEmail("mail@mail.ru");

        Mockito
                .when(mockUserRepository.existsById(Mockito.anyLong())).
                thenThrow(new EntityNotFoundException("Пользователь ID = " + user.getId() + " не найден."));

        final EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> userService.userExists(2L));

        Assertions.assertEquals("Пользователь ID = " + user.getId() + " не найден.", exception.getMessage());
    }
}
