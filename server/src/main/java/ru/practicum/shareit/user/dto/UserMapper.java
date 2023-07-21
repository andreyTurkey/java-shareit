package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public class UserMapper {

    public static UserDto getUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static User getUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static UserDto getUserDtoFromUpdateDto(UserUpdateDto userUpdateDto) {
        UserDto userDto = new UserDto();
        userDto.setId(userUpdateDto.getId());
        userDto.setName(userUpdateDto.getName());
        userDto.setEmail(userUpdateDto.getEmail());
        return userDto;
    }
}
