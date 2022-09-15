package ru.yandex.ewmservice.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmservice.event.dto.EventFullDto;
import ru.yandex.ewmservice.event.dto.EventShortDto;
import ru.yandex.ewmservice.event.requests.EventCreateRequest;
import ru.yandex.ewmservice.event.requests.EventUpdateRequest;
import ru.yandex.ewmservice.event.service.EventService;
import ru.yandex.ewmservice.event_request.dto.RequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
    public ResponseEntity<List<EventShortDto>> getEventsByUser(@PathVariable("userId") @Positive Long userId,
                                                               @RequestParam(value = "from", defaultValue = "0")
                                                               @PositiveOrZero Integer from,
                                                               @RequestParam(value = "size", defaultValue = "10")
                                                               @Positive Integer size) {
        return ResponseEntity.of(Optional.of(
                eventService.getEventsByUser(userId, from, size).getContent()
        ));
    }

    @PatchMapping
    public ResponseEntity<EventFullDto> updateEventByUser(@PathVariable("userId") @Positive Long userId,
                                                          @RequestBody @Valid EventUpdateRequest request) {
        return ResponseEntity.of(Optional.of(eventService.updateEventByUser(userId, request)));
    }


    @PostMapping
    public ResponseEntity<EventFullDto> createEvent(@PathVariable("userId") @Positive Long userId,
                                                    @RequestBody @Valid EventCreateRequest request) {
        return ResponseEntity.of(Optional.of(eventService.createEventByUser(userId, request)));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventOfUserById(@PathVariable("userId") @Positive Long userId,
                                                           @PathVariable("eventId") @Positive Long eventId) {
        return ResponseEntity.of(Optional.of(eventService.getEventOfUserById(userId, eventId)));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> cancelEventByUser(@PathVariable("userId") @Positive Long userId,
                                                          @PathVariable("eventId") @Positive Long eventId,
                                                          @RequestBody @Valid EventUpdateRequest request) {
        return ResponseEntity.of(Optional.of(eventService.cancelEventByUser(userId, eventId, request)));
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<RequestDto>> getAllRequestOfEvent(@PathVariable("userId") @Positive Long userId,
                                                                 @PathVariable("eventId") @Positive Long eventId) {
        return ResponseEntity.of(Optional.of(eventService.getRequestsByEvent(userId, eventId)));
    }

    @PatchMapping("/{eventId}/requests/{requestId}/confirm")
    public ResponseEntity<RequestDto> confirmRequest(@PathVariable("userId") @Positive Long userId,
                                                     @PathVariable("eventId") @Positive Long eventId,
                                                     @PathVariable("requestId") @Positive Long requestId) {
        return ResponseEntity.of(Optional.of(eventService.confirmRequest(userId, eventId, requestId)));
    }

    @PatchMapping("/{eventId}/requests/{requestId}/reject")
    public ResponseEntity<RequestDto> rejectRequest(@PathVariable("userId") @Positive Long userId,
                                                    @PathVariable("eventId") @Positive Long eventId,
                                                    @PathVariable("requestId") @Positive Long requestId) {
        return ResponseEntity.of(Optional.of(eventService.rejectRequest(userId, eventId, requestId)));
    }
}
