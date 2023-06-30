package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Comment;

@Component
public class CommentMapper {
    public CommentDto getCommentDto(Comment comment) {
        return new CommentDto.CommentDtoBuilder()
                .id(comment.getId())
                .authorName(comment.getUser().getName())
                .item(comment.getItem())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }

    public Comment getComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setItem(commentDto.getItem());
        comment.setText(commentDto.getText());
        comment.setUser(commentDto.getUser());
        comment.setCreated(commentDto.getCreated());
        return comment;
    }

    public CommentPublicDto getPublicCommentDto(Comment comment) {
        return new CommentPublicDto.CommentPublicDtoBuilder()
                .id(comment.getId())
                .created(comment.getCreated())
                .authorName(comment.getUser().getName())
                .text(comment.getText())
                .build();
    }
}