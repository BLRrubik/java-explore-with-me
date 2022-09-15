package ru.rubik.ewmservice.event.service;

import org.springframework.data.domain.Page;
import ru.rubik.ewmservice.event.dto.EventFullDto;
import ru.rubik.ewmservice.event.dto.EventShortDto;
import ru.rubik.ewmservice.event.filter.EventFilter;
import ru.rubik.ewmservice.event.requests.EventAdminUpdateRequest;
import ru.rubik.ewmservice.event.requests.EventCreateRequest;
import ru.rubik.ewmservice.event.requests.EventUpdateRequest;
import ru.rubik.ewmservice.event_request.dto.RequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {

    Page<EventShortDto> search(EventFilter filter, Integer from, Integer size, HttpServletRequest httpRequest);
    Page<EventFullDto> searchByAdmin(EventFilter filter, Integer from, Integer size, HttpServletRequest httpRequest);

    EventFullDto getEventById(Long eventId, HttpServletRequest httpRequest);

    Page<EventShortDto> getEventsByUser(Long userId, Integer from, Integer size, HttpServletRequest httpRequest);

    EventFullDto updateEventByUser(Long userId, EventUpdateRequest request, HttpServletRequest httpRequest);

    EventFullDto updateEventByAdmin(Long eventId, EventAdminUpdateRequest request, HttpServletRequest httpRequest);

    EventFullDto createEventByUser(Long userId, EventCreateRequest request, HttpServletRequest httpRequest);

    EventFullDto getEventOfUserById(Long userId, Long eventId, HttpServletRequest httpRequest);

    EventFullDto cancelEventByUser(Long userId, Long eventId, EventUpdateRequest request, HttpServletRequest httpRequest);

    EventFullDto publishEvent(Long eventId, HttpServletRequest httpRequest);

    EventFullDto rejectEvent(Long eventId, HttpServletRequest httpRequest);

    List<RequestDto> getRequestsByEvent(Long userId, Long eventId);

    RequestDto confirmRequest(Long userId, Long eventId, Long requestId);

    RequestDto rejectRequest(Long userId, Long eventId, Long requestId);

}
