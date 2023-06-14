package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    User getUserById(Long id);

    List<User> getAllUsers();

    User createUser(User newUser);

    User updateUser(User changedUser);

    void deleteUserById(Long id);

    void isUserExist(User user);

    void isUserExistById(Long id);
}
