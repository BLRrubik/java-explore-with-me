package ru.rubik.ewmservice.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rubik.ewmservice.event.dto.EventFullDto;
import ru.rubik.ewmservice.event.dto.EventShortDto;
import ru.rubik.ewmservice.event.filter.EventFilter;
import ru.rubik.ewmservice.event.service.EventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventPublicController {
    private final EventService eventService;

    @Autowired
    public EventPublicController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventShortDto>> search(@RequestParam("text") String text,
                                                      @RequestParam("categories") List<Long> categories,
                                                      @RequestParam("paid") Boolean paid,
                                                      @RequestParam("rangeStart")
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                          LocalDateTime rangeStart,
                                                      @RequestParam("rangeEnd")
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                          LocalDateTime rangeEnd,
                                                      //todo -> onlyAvailable, sort
                                                      @RequestParam("from") Integer from,
                                                      @RequestParam("size") Integer size) {
        EventFilter filter = new EventFilter();
        filter.setText(text);
        filter.setCategories(categories);
        filter.setPaid(paid);
        filter.setRangeStart(rangeStart);
        filter.setRangeEnd(rangeEnd);

        return ResponseEntity.of(Optional.of(eventService.search(filter, from, size).getContent()));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getById(@PathVariable("eventId") Long eventId) {
        return ResponseEntity.of(Optional.of(eventService.getEventById(eventId)));
    }
}
