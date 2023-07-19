package ru.practicum.shareit.user;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
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
        return userRepository.findAll().stream()
                .map(UserMapper::getUserDto)
                .collect(Collectors.toList());
    }

    public boolean isUserExists(Long userId)  {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new EntityNotFoundException("Пользователь ID = " + userId + " не найден.");
        } else {
            return true;
        }
    }
}

