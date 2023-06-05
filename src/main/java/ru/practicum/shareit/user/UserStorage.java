package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {

    User getUserById(Long id);

    List<User> getAllUsers();

    User createUser(User newUser);

    User updateUser(User changedUser);

    void deleteUserById(Long id);

    void isUserExist(User user);
}
