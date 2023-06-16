package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {

    final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        userService.addUser(user);
        log.trace(user + " - пользователь был добавлен");
        return user;
    }

    @PatchMapping(value = "/{userId}")
    public User updateUser(@PathVariable Long userId, @RequestBody User user) {
        user.setId(userId);
        userService.updateUser(user);
        log.debug("Получен запрос на обновление пользователя с  ID = {} ", userId);
        return userService.getUserById(userId);
    }

    @GetMapping(value = "/{userId}")
    public User getUserById(@PathVariable Long userId) {
        log.debug("Получен запрос  пользователя с  ID = {} ", userId);
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.debug("Получен запрос всех пользователей");
        return userService.getAllUsers();
    }

    @DeleteMapping(value = "{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        log.debug("Пользователь был удален");
    }
}
