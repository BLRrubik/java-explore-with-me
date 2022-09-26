package ru.yandex.ewmservice.comment.service.impl;

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
import ru.yandex.ewmservice.comment.service.CommentService;
import ru.yandex.ewmservice.event.exception.EventNotFoundException;
import ru.yandex.ewmservice.event.repository.EventRepository;
import ru.yandex.ewmservice.user.exception.UserNotFoundException;
import ru.yandex.ewmservice.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CommentDto createComment(CommentCreateRequest request, Long userId, Long eventId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("User with id " + userId + " is not found");
        }

        if (!eventRepository.findById(eventId).isPresent()) {
            throw new EventNotFoundException("Event with id " + eventId + " is not found");
        }

        if (!userRepository.findVisitedByEvent(userId, eventId).isPresent()) {
            throw new CommentUserNotVisitEventException("User with id " + userId +
                    " wasnt visit event with id " + eventId);
        }

        Comment comment = new Comment();

        comment.setText(request.getText());
        comment.setCreated(LocalDateTime.now());
        comment.setEdited(null);
        comment.setEvent(eventRepository.findById(eventId).get());
        comment.setAuthor(userRepository.findById(userId).get());

        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto updateComment(CommentUpdateRequest request, Long userId, Long eventId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("User with id " + userId + " is not found");
        }

        if (!eventRepository.findById(eventId).isPresent()) {
            throw new EventNotFoundException("Event with id " + eventId + " is not found");
        }

        if (!commentRepository.findById(request.getId()).isPresent()) {
            throw new CommentNotFoundException("Comment with id " + request.getId() + " is not found");
        }

        if (commentRepository.findByIdAndAuthorId(request.getId(), userId) == null) {
            throw new CommentAuthorException("Comment with id " + request.getId() +
                    " dont have author with id " + userId);
        }

        Comment comment = commentRepository.findById(request.getId()).get();

        comment.setText(request.getText());
        comment.setEdited(LocalDateTime.now());

        return CommentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public void deleteCommentByUser(Long userId, Long eventId, Long commentId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("User with id " + userId + " is not found");
        }

        if (!eventRepository.findById(eventId).isPresent()) {
            throw new EventNotFoundException("Event with id " + eventId + " is not found");
        }

        if (!commentRepository.findById(commentId).isPresent()) {
            throw new CommentNotFoundException("Comment with id " + commentId + " is not found");
        }

        if (commentRepository.findByIdAndAuthorId(commentId, userId) == null) {
            throw new CommentAuthorException("Comment with id " + commentId +
                    " dont have author with id " + userId);
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    public void deleteCommentByAdmin(Long eventId, Long commentId) {
        if (!eventRepository.findById(eventId).isPresent()) {
            throw new EventNotFoundException("Event with id " + eventId + " is not found");
        }

        if (!commentRepository.findById(commentId).isPresent()) {
            throw new CommentNotFoundException("Comment with id " + commentId + " is not found");
        }

        commentRepository.deleteById(commentId);
    }
}
