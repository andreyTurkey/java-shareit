package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * TODO Sprint add-controllers.
 */
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
        return user;
    }

    @PatchMapping(value = "/{userId}")
    public User updateUser(@Valid @PathVariable Long userId, @RequestBody User user) {
        user.setId(userId);
        userService.updateUser(user);
        return userService.getUserById(userId);
    }

    @GetMapping(value = "/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }
}
