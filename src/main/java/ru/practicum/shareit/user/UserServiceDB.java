package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

public interface UserServiceDB {

    UserDto addUser(UserDto userDto);

    UserDto updateUser(UserUpdateDto userDto);

    UserDto getUserById(Long userId);

    void deleteUser(Long userId);

    List<UserDto> getAllUsers();
}
