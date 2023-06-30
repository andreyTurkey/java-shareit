package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

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
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        log.trace(userDto + " - пользователь был добавлен");
        return userService.addUser(userDto);
    }

    @PatchMapping(value = "/{userId}")
    public UserDto updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        userDto.setId(userId);
        userService.updateUser(userDto);
        log.debug("Получен запрос на обновление пользователя с  ID = {} ", userId);
        return userService.getUserById(userId);
    }

    @GetMapping(value = "/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.debug("Получен запрос  пользователя с  ID = {} ", userId);
        return userService.getUserById(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.debug("Получен запрос всех пользователей");
        return userService.getAllUsers();
    }

    @DeleteMapping(value = "{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        log.debug("Пользователь был удален");
    }
}
