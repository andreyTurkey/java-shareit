package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto getUserById(Long userId) {
        return UserMapper.getUserDto(userRepository.getReferenceById(userId));
    }

    public UserDto addUser(UserDto userDto) {
        return UserMapper.getUserDto(userRepository.save(UserMapper.getUser(userDto)));
    }

    public UserDto updateUser(UserUpdateDto userUpdateDto) {
        User user = userRepository.getReferenceById(userUpdateDto.getId());
        if (userUpdateDto.getName() != null) user.setName(userUpdateDto.getName());
        if (userUpdateDto.getEmail() != null) user.setEmail(userUpdateDto.getEmail());
        return UserMapper.getUserDto(userRepository.save(user));
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserMapper::getUserDto).collect(Collectors.toList());
    }
}
