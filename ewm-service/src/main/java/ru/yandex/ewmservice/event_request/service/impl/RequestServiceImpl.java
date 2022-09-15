package ru.yandex.ewmservice.event_request.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.ewmservice.event.entity.Event;
import ru.yandex.ewmservice.event.entity.EventState;
import ru.yandex.ewmservice.event.exception.EventNotFoundException;
import ru.yandex.ewmservice.event.repository.EventRepository;
import ru.yandex.ewmservice.event_request.dto.RequestDto;
import ru.yandex.ewmservice.event_request.entity.Request;
import ru.yandex.ewmservice.event_request.entity.RequestStatus;
import ru.yandex.ewmservice.event_request.exception.RequestIllegalStateException;
import ru.yandex.ewmservice.event_request.exception.RequestNotFoundException;
import ru.yandex.ewmservice.event_request.mapper.RequestMapper;
import ru.yandex.ewmservice.event_request.repository.RequestRepository;
import ru.yandex.ewmservice.event_request.service.RequestService;
import ru.yandex.ewmservice.user.exception.UserNotFoundException;
import ru.yandex.ewmservice.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<RequestDto> getAllByUser(Long userId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("User with id " + userId + " is not found");
        }

        return RequestMapper.toDtos(requestRepository.findByRequesterId(userId));
    }

    @Override
    public RequestDto createRequest(Long userId, Long eventId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("User with id " + userId + " is not found");
        }

        if (!eventRepository.findById(eventId).isPresent()) {
            throw new EventNotFoundException("Event with id " + eventId + " is not found");
        }

        Request request = new Request();

        Event event = eventRepository.findById(eventId).get();

        if (requestRepository.findByRequesterIdAndEventId(userId, eventId) != null) {
            throw new RequestIllegalStateException("Request is already exists");
        }

        if (event.getInitiator().getId().equals(userId)) {
            throw new RequestIllegalStateException("User with id " + userId + " is owner of event with id " + eventId);
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new RequestIllegalStateException("Event with id " + eventId + " is not publish");
        }

        boolean limitState = event.getParticipantLimit() != 0 &&
                event.getParticipantLimit() == requestRepository.findByEventIdAndStatus(eventId, RequestStatus.APPROVED)
                        .size();

        if (limitState) {
            throw new RequestIllegalStateException("Event with id " + eventId + " reached participant limit");
        }

        if (!event.getRequestModeration()) {
            request.setStatus(RequestStatus.APPROVED);
        } else {
            request.setStatus(RequestStatus.PENDING);
        }

        request.setRequester(userRepository.findById(userId).get());
        request.setCreated(LocalDateTime.now());
        request.setEvent(eventRepository.findById(eventId).get());

        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public RequestDto cancelRequestByUser(Long userId, Long requestId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("User with id " + userId + " is not found");
        }

        if (!requestRepository.findById(requestId).isPresent()) {
            throw new RequestNotFoundException("Request with id " + requestId + " is not found");
        }

        Request request = requestRepository.findById(requestId).get();

        request.setStatus(RequestStatus.REJECT);

        return RequestMapper.toDto(requestRepository.save(request));
    }
}
