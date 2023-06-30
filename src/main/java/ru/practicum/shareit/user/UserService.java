package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    private final UserMapper userMapper;

    @Autowired
    public UserService(UserStorage userStorage, UserMapper userMapper) {
        this.userStorage = userStorage;
        this.userMapper = userMapper;
    }

    public UserDto addUser(UserDto newUser) {
        return userMapper.getUserDto(userStorage.createUser(userMapper.getUser(newUser)));
    }

    public UserDto updateUser(UserDto changedUser) {
        userStorage.updateUser(userMapper.getUser(changedUser));
        return changedUser;
    }

    public UserDto getUserById(Long id) {
        return userMapper.getUserDto(userStorage.getUserById(id));
    }

    public void deleteUser(Long id) {
        userStorage.deleteUserById(id);
    }

    List<UserDto> getAllUsers() {
        return userStorage.getAllUsers().stream().map(userMapper::getUserDto).collect(Collectors.toList());
    }
}
