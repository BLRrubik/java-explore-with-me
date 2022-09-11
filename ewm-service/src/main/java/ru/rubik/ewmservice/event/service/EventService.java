package ru.rubik.ewmservice.event.service;

import org.springframework.data.domain.Page;
import ru.rubik.ewmservice.event.dto.EventFullDto;
import ru.rubik.ewmservice.event.dto.EventShortDto;
import ru.rubik.ewmservice.event.filter.EventFilter;
import ru.rubik.ewmservice.event.requests.EventAdminUpdateRequest;
import ru.rubik.ewmservice.event.requests.EventCreateRequest;
import ru.rubik.ewmservice.event.requests.EventUpdateRequest;
import ru.rubik.ewmservice.event_request.dto.RequestDto;

import java.util.List;

public interface EventService {

    Page<EventShortDto> search(EventFilter filter, Integer from, Integer size);
    Page<EventFullDto> searchByAdmin(EventFilter filter, Integer from, Integer size);

    EventFullDto getEventById(Long eventId);

    Page<EventShortDto> getEventsByUser(Long userId, Integer from, Integer size);

    EventFullDto updateEventByUser(Long userId, EventUpdateRequest request);

    EventFullDto updateEventByAdmin(Long eventId, EventAdminUpdateRequest request);

    EventFullDto createEventByUser(Long userId, EventCreateRequest request);

    EventFullDto getEventOfUserById(Long userId, Long eventId);

    EventFullDto cancelEventByUser(Long userId, Long eventId, EventUpdateRequest request);

    EventFullDto publishEvent(Long eventId);

    EventFullDto rejectEvent(Long eventId);

    List<RequestDto> getRequestsByEvent(Long userId, Long eventId);

    RequestDto confirmRequest(Long userId, Long eventId, Long requestId);

    RequestDto rejectRequest(Long userId, Long eventId, Long requestId);

}
