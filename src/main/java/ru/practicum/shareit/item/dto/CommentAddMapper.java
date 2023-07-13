package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentAddMapper {

    public static CommentDto getCommentDto(CommentAddDto commentAddDto, User user, Item item) {

       /* return new CommentDto.CommentDtoBuilder()
                .id(commentAddDto.getId())
                .item(item)
                .user(user)
                .authorName(user.getName())
                .text(commentAddDto.getText())
                .created(commentAddDto.getCreated())
                .build();*/

        CommentDto commentDto = new CommentDto();
        commentDto.setId(commentAddDto.getId());
        commentDto.setItem(item);
        commentDto.setUser(user);
        commentDto.setAuthorName(user.getName());
        commentDto.setText(commentAddDto.getText());
        commentDto.setCreated(commentAddDto.getCreated());

        return commentDto;
    }
}
