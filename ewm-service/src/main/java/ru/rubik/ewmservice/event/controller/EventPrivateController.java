package ru.rubik.ewmservice.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rubik.ewmservice.event.dto.EventFullDto;
import ru.rubik.ewmservice.event.dto.EventShortDto;
import ru.rubik.ewmservice.event.requests.EventCreateRequest;
import ru.rubik.ewmservice.event.requests.EventUpdateRequest;
import ru.rubik.ewmservice.event.service.EventService;
import ru.rubik.ewmservice.event_request.dto.RequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users/{userId}/events/")
public class EventPrivateController {
    private final EventService eventService;

    @Autowired
    public EventPrivateController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEventsByUser(@PathVariable("userId") Long userId,
                                                               @RequestParam(value = "from", defaultValue = "0")
                                                                   Integer from,
                                                               @RequestParam(value = "size", defaultValue = "10")
                                                                   Integer size,
                                                               HttpServletRequest httpRequest) {
        return ResponseEntity.of(Optional.of(
                eventService.getEventsByUser(userId, from, size, httpRequest).getContent()
        ));
    }

    @PatchMapping
    public ResponseEntity<EventFullDto> updateEventByUser(@PathVariable("userId") Long userId,
                                                          @RequestBody EventUpdateRequest request,
                                                          HttpServletRequest httpRequest) {
        return ResponseEntity.of(Optional.of(eventService.updateEventByUser(userId, request, httpRequest)));
    }


    @PostMapping
    public ResponseEntity<EventFullDto> createEvent(@PathVariable("userId") Long userId,
                                                    @RequestBody EventCreateRequest request,
                                                    HttpServletRequest httpRequest) {
        return ResponseEntity.of(Optional.of(eventService.createEventByUser(userId, request, httpRequest)));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventOfUserById(@PathVariable("userId") Long userId,
                                                           @PathVariable("eventId") Long eventId,
                                                           HttpServletRequest httpRequest) {
        return ResponseEntity.of(Optional.of(eventService.getEventOfUserById(userId, eventId, httpRequest)));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> cancelEventByUser(@PathVariable("userId") Long userId,
                                                          @PathVariable("eventId") Long eventId,
                                                          @RequestBody EventUpdateRequest request,
                                                          HttpServletRequest httpRequest) {
        return ResponseEntity.of(Optional.of(eventService.cancelEventByUser(userId, eventId, request, httpRequest)));
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<RequestDto>> getAllRequestOfEvent(@PathVariable("userId") Long userId,
                                                                 @PathVariable("eventId") Long eventId) {
        return ResponseEntity.of(Optional.of(eventService.getRequestsByEvent(userId, eventId)));
    }

    @PatchMapping("/{eventId}/requests/{requestId}/confirm")
    public ResponseEntity<RequestDto> confirmRequest(@PathVariable("userId") Long userId,
                                                     @PathVariable("eventId") Long eventId,
                                                     @PathVariable("requestId") Long requestId) {
        return ResponseEntity.of(Optional.of(eventService.confirmRequest(userId, eventId, requestId)));
    }

    @PatchMapping("/{eventId}/requests/{requestId}/reject")
    public ResponseEntity<RequestDto> rejectRequest(@PathVariable("userId") Long userId,
                                                     @PathVariable("eventId") Long eventId,
                                                     @PathVariable("requestId") Long requestId) {
        return ResponseEntity.of(Optional.of(eventService.rejectRequest(userId, eventId, requestId)));
    }
}
