package ru.rubik.ewmservice.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rubik.ewmservice.event.dto.EventFullDto;
import ru.rubik.ewmservice.event.dto.EventShortDto;
import ru.rubik.ewmservice.event.requests.EventCreateRequest;
import ru.rubik.ewmservice.event.requests.EventUpdateRequest;
import ru.rubik.ewmservice.event.service.EventService;

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
                                                                   Integer size) {
        return ResponseEntity.of(Optional.of(eventService.getEventsByUser(userId, from, size).getContent()));
    }

    @PatchMapping
    public ResponseEntity<EventFullDto> updateEventByUser(@PathVariable("userId") Long userId,
                                                          @RequestBody EventUpdateRequest request) {
        return ResponseEntity.of(Optional.of(eventService.updateEventByUser(userId, request)));
    }


    @PostMapping
    public ResponseEntity<EventFullDto> createEvent(@PathVariable("userId") Long userId,
                                                    @RequestBody EventCreateRequest request) {
        return ResponseEntity.of(Optional.of(eventService.createEventByUser(userId, request)));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventOfUserById(@PathVariable("userId") Long userId,
                                                           @PathVariable("eventId") Long eventId) {
        return ResponseEntity.of(Optional.of(eventService.getEventOfUserById(userId, eventId)));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> cancelEventByUser(@PathVariable("userId") Long userId,
                                                          @PathVariable("eventId") Long eventId,
                                                          @RequestBody EventUpdateRequest request) {
        return ResponseEntity.of(Optional.of(eventService.cancelEventByUser(userId, eventId, request)));
    }

    //todo -> requests endpoints
}
