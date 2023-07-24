package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        log.debug(userDto + " - пользователь был добавлен");
        log.error(userDto + " - ЗАПРОС НА ДОБАВЛЕНИЕ ПОЛЬЗОВАТЕЛЯ");
        return userService.addUser(userDto);
    }

    @PatchMapping(value = "/{userId}")
    public UserDto updateUser(@PathVariable Long userId, @RequestBody UserUpdateDto userUpdateDto) {
        userUpdateDto.setId(userId);
        userService.updateUser(userUpdateDto);
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
