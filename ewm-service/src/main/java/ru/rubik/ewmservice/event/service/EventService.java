package ru.rubik.ewmservice.event.service;

import org.springframework.data.domain.Page;
import ru.rubik.ewmservice.event.dto.EventFullDto;
import ru.rubik.ewmservice.event.dto.EventShortDto;
import ru.rubik.ewmservice.event.requests.EventCreateRequest;
import ru.rubik.ewmservice.event.requests.EventUpdateRequest;

import java.util.List;

public interface EventService {

    EventFullDto getEventById(Long eventId);

    Page<EventShortDto> getEventsByUser(Long userId, Integer from, Integer size);

    EventFullDto updateEventByUser(Long userId, EventUpdateRequest request);

    EventFullDto createEventByUser(Long userId, EventCreateRequest request);

    EventFullDto getEventOfUserById(Long userId, Long eventId);

}
