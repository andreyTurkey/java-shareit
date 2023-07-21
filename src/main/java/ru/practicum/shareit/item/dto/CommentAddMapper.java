package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentAddMapper {

    public static CommentDto getCommentDto(CommentAddDto commentAddDto, User user, Item item) {

        CommentDto commentDto = new CommentDto();
        commentDto.setId(commentAddDto.getId());
        commentDto.setItem(item);
        commentDto.setUser(user);
        commentDto.setAuthorName(user.getName());
        commentDto.setText(commentAddDto.getText());
        commentDto.setCreated(commentAddDto.getCreated());

        return commentDto;
    }

    public static CommentAddDto getCommentAddDto(Comment comment) {

        CommentAddDto commentAddDto = new CommentAddDto();
        commentAddDto.setId(comment.getId());
        commentAddDto.setItemId(comment.getItem().getId());
        commentAddDto.setUserId(comment.getUser().getId());
        commentAddDto.setText(comment.getText());
        commentAddDto.setCreated(comment.getCreated());

        return commentAddDto;
    }
}
