package ru.rubik.ewmservice.event.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rubik.ewmservice.event.dto.EventFullDto;
import ru.rubik.ewmservice.event.dto.EventShortDto;
import ru.rubik.ewmservice.event.filter.EventFilter;
import ru.rubik.ewmservice.event.filter.EventSort;
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
    public ResponseEntity<List<EventShortDto>> search(@RequestParam(value = "text", required = false)
                                                          String text,
                                                      @RequestParam(value = "categories", required = false)
                                                          List<Long> categories,
                                                      @RequestParam(value = "paid", required = false)
                                                          Boolean paid,
                                                      @RequestParam(value = "rangeStart", required = false)
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                          LocalDateTime rangeStart,
                                                      @RequestParam(value = "rangeEnd", required = false)
                                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                          LocalDateTime rangeEnd,
                                                      @RequestParam(value = "sort", required = false)
                                                          EventSort sort,
                                                      @RequestParam(value = "onlyAvailable", required = false)
                                                          Boolean onlyAvailable,
                                                      @RequestParam(value = "from", defaultValue = "0")
                                                          Integer from,
                                                      @RequestParam(value = "size", defaultValue = "10")
                                                          Integer size) {

        EventFilter filter = new EventFilter();
        filter.setText(text);
        filter.setCategories(categories);
        filter.setPaid(paid);
        filter.setRangeStart(rangeStart);
        filter.setRangeEnd(rangeEnd);
        filter.setOnlyAvailable(onlyAvailable);
        filter.setSort(sort);

        return ResponseEntity.of(Optional.of(eventService.search(filter, from, size).getContent()));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getById(@PathVariable("eventId") Long eventId) {
        return ResponseEntity.of(Optional.of(eventService.getEventById(eventId)));
    }
}
