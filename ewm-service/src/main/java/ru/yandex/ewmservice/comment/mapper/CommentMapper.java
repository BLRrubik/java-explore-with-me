package ru.yandex.ewmservice.comment.mapper;

import ru.yandex.ewmservice.comment.dto.CommentDto;
import ru.yandex.ewmservice.comment.entity.Comment;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
    public static CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getCreated(),
                comment.getEdited(),
                comment.getAuthor().getId(),
                comment.getEvent().getId()
        );
    }

    public static List<CommentDto> toDtos(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }
}
