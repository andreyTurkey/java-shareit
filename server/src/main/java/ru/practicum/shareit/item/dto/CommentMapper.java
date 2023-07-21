package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;

public class CommentMapper {

    public static CommentDto getCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setAuthorName(comment.getUser().getName());
        commentDto.setItem(comment.getItem());
        commentDto.setText(comment.getText());
        commentDto.setCreated(comment.getCreated());

        return commentDto;

    }

    public static Comment getComment(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setItem(commentDto.getItem());
        comment.setText(commentDto.getText());
        comment.setUser(commentDto.getUser());
        comment.setCreated(commentDto.getCreated());
        return comment;
    }

    public static CommentPublicDto getPublicCommentDto(Comment comment) {
        return new CommentPublicDto.CommentPublicDtoBuilder()
                .id(comment.getId())
                .created(comment.getCreated())
                .authorName(comment.getUser().getName())
                .text(comment.getText())
                .build();
    }
}
