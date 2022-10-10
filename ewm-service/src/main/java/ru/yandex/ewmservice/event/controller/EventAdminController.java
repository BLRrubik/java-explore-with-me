package ru.yandex.ewmservice.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmservice.event.dto.EventFullDto;
import ru.yandex.ewmservice.event.entity.EventState;
import ru.yandex.ewmservice.event.filter.EventFilter;
import ru.yandex.ewmservice.event.requests.EventAdminUpdateRequest;
import ru.yandex.ewmservice.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
                                                            @PositiveOrZero Integer from,
                                                            @RequestParam(value = "size", defaultValue = "10")
                                                            @Positive Integer size) {

        EventFilter filter = new EventFilter();
        filter.setUsers(users);
        filter.setStates(states);
        filter.setCategories(categories);
        filter.setRangeStart(rangeStart);
        filter.setRangeEnd(rangeEnd);

        return ResponseEntity.of(Optional.of(eventService.searchByAdmin(filter, from, size)));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEventByAdmin(@PathVariable("eventId") @Positive Long eventId,
                                                           @RequestBody @Valid EventAdminUpdateRequest request) {
        return ResponseEntity.of(Optional.of(eventService.updateEventByAdmin(eventId, request)));
    }

    @PatchMapping("/{eventId}/publish")
    public ResponseEntity<EventFullDto> publishEvent(@PathVariable("eventId") @Positive Long eventId) {
        return ResponseEntity.of(Optional.of(eventService.publishEvent(eventId)));
    }

    @PatchMapping("/{eventId}/reject")
    public ResponseEntity<EventFullDto> rejectEvent(@PathVariable("eventId") @Positive Long eventId) {
        return ResponseEntity.of(Optional.of(eventService.rejectEvent(eventId)));
    }
}
