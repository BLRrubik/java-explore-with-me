package ru.yandex.ewmservice.comment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.ewmservice.comment.dto.CommentDto;
import ru.yandex.ewmservice.comment.entity.Comment;
import ru.yandex.ewmservice.comment.exception.CommentAuthorException;
import ru.yandex.ewmservice.comment.exception.CommentNotFoundException;
import ru.yandex.ewmservice.comment.exception.CommentUserNotVisitEventException;
import ru.yandex.ewmservice.comment.mapper.CommentMapper;
import ru.yandex.ewmservice.comment.repository.CommentRepository;
import ru.yandex.ewmservice.comment.requests.CommentCreateRequest;
import ru.yandex.ewmservice.comment.requests.CommentUpdateRequest;
import ru.yandex.ewmservice.event.entity.Event;
import ru.yandex.ewmservice.event.exception.EventNotFoundException;
import ru.yandex.ewmservice.event.repository.EventRepository;
import ru.yandex.ewmservice.user.entity.User;
import ru.yandex.ewmservice.user.exception.UserNotFoundException;
import ru.yandex.ewmservice.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class CommentService{
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public CommentDto createComment(CommentCreateRequest request, Long userId, Long eventId) {
        if (!eventRepository.findById(eventId).isPresent()) {
            throw new EventNotFoundException("Event with id " + eventId + " is not found");
        }

        if (!userRepository.findVisitedByEvent(userId, eventId).isPresent()) {
            throw new CommentUserNotVisitEventException(userId, eventId);
        }

        Comment comment = new Comment();
        Event event = eventRepository.findById(eventId).get();
        User author = userRepository.findById(userId).get();

        comment.setText(request.getText());
        comment.setCreated(LocalDateTime.now());
        comment.setEdited(null);
        comment.setEvent(event);
        comment.setAuthor(author);

        return CommentMapper.toDto(commentRepository.save(comment));
    }

    public CommentDto updateComment(CommentUpdateRequest request, Long userId) {
        if (commentRepository.findByIdAndAuthorId(request.getId(), userId) == null) {
            throw new CommentAuthorException(request.getId(), userId);
        }

        Comment comment = commentRepository.findById(request.getId()).get();

        comment.setText(request.getText());
        comment.setEdited(LocalDateTime.now());

        return CommentMapper.toDto(commentRepository.save(comment));
    }

    public void deleteCommentByUser(Long userId, Long commentId) {
        if (commentRepository.findByIdAndAuthorId(commentId, userId) == null) {
            throw new CommentAuthorException(commentId, userId);
        }

        commentRepository.deleteById(commentId);
    }

    public void deleteCommentByAdmin(Long commentId) {
        if (!commentRepository.findById(commentId).isPresent()) {
            throw new CommentNotFoundException(commentId);
        }

        commentRepository.deleteById(commentId);
    }
}
