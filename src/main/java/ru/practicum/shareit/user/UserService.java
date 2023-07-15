package ru.practicum.shareit.user;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {

    final UserRepository userRepository;

    public UserDto getUserById(Long userId) {
        return UserMapper.getUserDto(userRepository.getReferenceById(userId));
    }

    public UserDto addUser(UserDto userDto) {
        return UserMapper.getUserDto(userRepository.save(UserMapper.getUser(userDto)));
    }

    public UserDto updateUser(UserUpdateDto userUpdateDto) {
        User user = userRepository.getReferenceById(userUpdateDto.getId());
        if (userUpdateDto.getName() != null) {
            user.setName(userUpdateDto.getName());
        }
        if (userUpdateDto.getEmail() != null) {
            user.setEmail(userUpdateDto.getEmail());
        }
        return UserMapper.getUserDto(userRepository.save(user));
    }

    public boolean deleteUser(Long userId) {
        userRepository.deleteById(userId);
        return true;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::getUserDto).collect(Collectors.toList());
    }

    /*public boolean isUserExists(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь ID = " + userId + " не найден.");
        }
        return true;
    }*/

    public boolean isUserExists(Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("Пользователь ID = " + userId + " не найден."));
        /*if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("Пользователь ID = " + userId + " не найден.");
        }*/
        return true;
    }

    public boolean isUserExistsByEmail(String email) {
        userRepository.findByEmail(email).orElseThrow(() ->
                new EntityNotFoundException("Пользователь ID = " + email + " не найден."));
        return true;
    }
}
