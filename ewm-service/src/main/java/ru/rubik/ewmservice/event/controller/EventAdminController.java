package ru.rubik.ewmservice.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rubik.ewmservice.event.dto.EventFullDto;
import ru.rubik.ewmservice.event.entity.EventState;
import ru.rubik.ewmservice.event.filter.EventFilter;
import ru.rubik.ewmservice.event.requests.EventAdminUpdateRequest;
import ru.rubik.ewmservice.event.service.EventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/events")
public class EventAdminController {
    private final EventService eventService;

    @Autowired
    public EventAdminController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventFullDto>> searchByAdmin(@RequestParam("users") List<Long> users,
                                                            @RequestParam("states") List<EventState> states,
                                                            @RequestParam("categories") List<Long> categories,
                                                            @RequestParam("rangeStart")
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                LocalDateTime rangeStart,
                                                            @RequestParam("rangeEnd")
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                LocalDateTime rangeEnd,
                                                            @RequestParam("from") Integer from,
                                                            @RequestParam("size") Integer size) {
        EventFilter filter = new EventFilter();
        filter.setUsers(users);
        filter.setStates(states);
        filter.setCategories(categories);
        filter.setRangeStart(rangeStart);
        filter.setRangeEnd(rangeEnd);

        return ResponseEntity.of(Optional.of(eventService.searchByAdmin(filter, from, size).getContent()));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEventByAdmin(@PathVariable("eventId") Long eventId,
                                                           @RequestBody EventAdminUpdateRequest request) {
        return ResponseEntity.of(Optional.of(eventService.updateEventByAdmin(eventId, request)));
    }

    @PatchMapping("/{eventId}/publish")
    public ResponseEntity<EventFullDto> publishEvent(@PathVariable("eventId") Long eventId) {
        return ResponseEntity.of(Optional.of(eventService.publishEvent(eventId)));
    }

    @PatchMapping("/{eventId}/reject")
    public ResponseEntity<EventFullDto> rejectEvent(@PathVariable("eventId") Long eventId) {
        return ResponseEntity.of(Optional.of(eventService.rejectEvent(eventId)));
    }
}
