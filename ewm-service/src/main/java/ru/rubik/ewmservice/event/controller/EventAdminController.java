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

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<List<EventFullDto>> searchByAdmin(@RequestParam(value = "users", required = false)
                                                                List<Long> users,
                                                            @RequestParam(value = "states", required = false)
                                                                List<EventState> states,
                                                            @RequestParam(value = "categories", required = false)
                                                                List<Long> categories,
                                                            @RequestParam(value = "rangeStart", required = false)
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                LocalDateTime rangeStart,
                                                            @RequestParam(value = "rangeEnd", required = false)
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                LocalDateTime rangeEnd,
                                                            @RequestParam(value = "from", defaultValue = "0")
                                                                Integer from,
                                                            @RequestParam(value = "size", defaultValue = "10")
                                                                Integer size,
                                                            HttpServletRequest httpRequest) {

        EventFilter filter = new EventFilter();
        filter.setUsers(users);
        filter.setStates(states);
        filter.setCategories(categories);
        filter.setRangeStart(rangeStart);
        filter.setRangeEnd(rangeEnd);

        return ResponseEntity.of(Optional.of(eventService.searchByAdmin(filter, from, size, httpRequest).getContent()));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEventByAdmin(@PathVariable("eventId") Long eventId,
                                                           @RequestBody EventAdminUpdateRequest request,
                                                           HttpServletRequest httpRequest) {
        return ResponseEntity.of(Optional.of(eventService.updateEventByAdmin(eventId, request, httpRequest)));
    }

    @PatchMapping("/{eventId}/publish")
    public ResponseEntity<EventFullDto> publishEvent(@PathVariable("eventId") Long eventId,
                                                     HttpServletRequest httpRequest) {
        return ResponseEntity.of(Optional.of(eventService.publishEvent(eventId, httpRequest)));
    }

    @PatchMapping("/{eventId}/reject")
    public ResponseEntity<EventFullDto> rejectEvent(@PathVariable("eventId") Long eventId,
                                                    HttpServletRequest httpRequest) {
        return ResponseEntity.of(Optional.of(eventService.rejectEvent(eventId, httpRequest)));
    }
}
