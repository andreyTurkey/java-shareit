package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

public class UserMapper {

    public static UserDto getUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;

      /*  return new UserDto.UserDtoBuilder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();*/
    }

    public static User getUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public static UserDto getUserDtoFromUpdateDto(UserUpdateDto userUpdateDto) {
        /*return new UserDto.UserDtoBuilder()
                .id(userUpdateDto.getId())
                .name(userUpdateDto.getName())
                .email(userUpdateDto.getEmail())
                .build();*/
        UserDto userDto = new UserDto();
        userDto.setId(userUpdateDto.getId());
        userDto.setName(userUpdateDto.getName());
        userDto.setEmail(userUpdateDto.getEmail());
        return userDto;
    }
}
