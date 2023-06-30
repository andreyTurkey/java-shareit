package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.UserServiceDB;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class CommentAddMapper {

    UserServiceDB userServiceDB;

    GettingItem gettingItem;

    UserMapper userMapper;

    @Autowired
    public CommentAddMapper(UserServiceDB userServiceDB, GettingItem gettingItem, UserMapper userMapper) {
        this.userServiceDB = userServiceDB;
        this.gettingItem = gettingItem;
        this.userMapper = userMapper;
    }

    public CommentDto getCommentDto(CommentAddDto commentAddDto) {
        User user = userMapper.getUser(userServiceDB.getUserById(commentAddDto.getUserId()));
        return new CommentDto.CommentDtoBuilder()
                .id(commentAddDto.getId())
                .item(gettingItem.getItemById(commentAddDto.getItemId()))
                .user(user)
                .authorName(user.getName())
                .text(commentAddDto.getText())
                .created(commentAddDto.getCreated())
                .build();
    }
}
