package ru.rubik.ewmservice.event.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.rubik.ewmservice.category.exception.CategoryNotFoundException;
import ru.rubik.ewmservice.category.repository.CategoryRepository;
import ru.rubik.ewmservice.event.dto.EventFullDto;
import ru.rubik.ewmservice.event.dto.EventShortDto;
import ru.rubik.ewmservice.event.entity.Event;
import ru.rubik.ewmservice.event.entity.EventState;
import ru.rubik.ewmservice.event.exception.EventNotFoundException;
import ru.rubik.ewmservice.event.mapper.EventMapper;
import ru.rubik.ewmservice.event.repository.EventRepository;
import ru.rubik.ewmservice.event.requests.EventCreateRequest;
import ru.rubik.ewmservice.event.requests.EventUpdateRequest;
import ru.rubik.ewmservice.event.service.EventService;
import ru.rubik.ewmservice.user.exception.UserNotFoundException;
import ru.rubik.ewmservice.user.repository.UserRepository;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public EventFullDto getEventById(Long eventId) {
        if (!eventRepository.findById(eventId).isPresent()) {
            throw new EventNotFoundException("Event with id " + eventId + " is not found");
        }

        //todo -> request to stats

        return EventMapper.toFullDto(eventRepository.findByIdAndState(eventId, EventState.PUBLISHED));
    }

    @Override
    public Page<EventShortDto> getEventsByUser(Long userId, Integer from, Integer size) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("User with id " + userId + "is not found");
        }

        Page<Event> events = eventRepository.findByInitiatorId(userId, PageRequest.of(from / size, size));

        return EventMapper.convertPageToShortDto(events);
    }

    @Override
    public EventFullDto updateEventByUser(Long userId, EventUpdateRequest request) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("User with id " + userId + "is not found");
        }

        if (!categoryRepository.findById(request.getCategory()).isPresent()) {
            throw new CategoryNotFoundException("Category with id " + request.getCategory() + " is not found");
        }

        if (!eventRepository.findById(request.getEventId()).isPresent()) {
            throw new EventNotFoundException("Event with id " + request.getEventId() + " is not found");
        }

        Event event = eventRepository.findById(request.getEventId()).get();

        event.setAnnotation(request.getAnnotation());
        event.setCategory(categoryRepository.findById(request.getCategory()).get());
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());
        event.setPaid(request.getPaid());
        event.setParticipantLimit(request.getParticipantLimit());
        event.setTitle(request.getTitle());

        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto createEventByUser(Long userId, EventCreateRequest request) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("User with id " + userId + "is not found");
        }
        if (!categoryRepository.findById(request.getCategory()).isPresent()) {
            throw new CategoryNotFoundException("Category with id " + request.getCategory() + " is not found");
        }

        Event event = new Event();

        event.setAnnotation(request.getAnnotation());
        event.setCategory(categoryRepository.findById(request.getCategory()).get());
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());
        event.setPaid(request.getPaid());
        event.setParticipantLimit(request.getParticipantLimit());
        event.setRequestModeration(request.getRequestModeration());
        event.setTitle(request.getTitle());

        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getEventOfUserById(Long userId, Long eventId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("User with id " + userId + "is not found");
        }

        return EventMapper.toFullDto(eventRepository.findByIdAndInitiatorId(eventId, userId));
    }
}
