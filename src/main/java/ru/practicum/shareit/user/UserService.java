package ru.practicum.shareit.user;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Data
@Component
public class UserService {

    private final UserStorage userStorage;

    private Long count;

    @Autowired
    public UserService(@Qualifier("UserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
        count = 1L;
    }

    public User addUser(User newUser) {
        newUser.setId(count);
        userStorage.createUser(newUser);
        count++;
        return newUser;
    }

    public User updateUser(User changedUser) {
        userStorage.isUserExist(changedUser);
        userStorage.updateUser(changedUser);
        return changedUser;
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    public void deleteUser(Long id) {
        userStorage.deleteUserById(id);
    }

    List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }
}
