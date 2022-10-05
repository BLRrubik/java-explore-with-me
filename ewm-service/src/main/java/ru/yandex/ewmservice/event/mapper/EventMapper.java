package ru.yandex.ewmservice.event.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import ru.yandex.ewmservice.client.event.EventClient;
import ru.yandex.ewmservice.comment.repository.CommentRepository;
import ru.yandex.ewmservice.event.dto.EventFullDto;
import ru.yandex.ewmservice.event.dto.EventShortDto;
import ru.yandex.ewmservice.event.entity.Event;
import ru.yandex.ewmservice.event.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventMapper {

    private static EventClient eventClient;
    private static EventRepository eventRepository;
    private static CommentRepository commentRepository;

    @Autowired
    public EventMapper(EventClient eventClient, EventRepository eventRepository, CommentRepository commentRepository) {
        this.eventClient = eventClient;
        this.eventRepository = eventRepository;
        this.commentRepository = commentRepository;
    }


    public static EventFullDto toFullDto(Event event) {
        return new EventFullDto(event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getAnnotation(),
                event.getCreatedOn(),
                event.getEventDate(),
                event.getPublishedOn(),
                event.getPaid(),
                event.getState(),
                event.getRequestModeration(),
                event.getParticipantLimit(),
                new EventFullDto.Category(
                        event.getCategory().getId(),
                        event.getCategory().getName()),
                new EventFullDto.User(
                        event.getInitiator().getId(),
                        event.getInitiator().getName()),
                eventClient.getStats(
                        List.of("/events/" + event.getId())).stream()
                        .filter(stat -> stat.getUri().split("/")[2].equals(event.getId().toString()))
                        .findFirst()
                        .get()
                        .getHits(),
                eventRepository.countApprovedRequests(
                        event.getId()),
                new EventFullDto.Location(
                        event.getLatitude(),
                        event.getLongitude()),
                commentRepository.findByEventId(event.getId()).stream()
                        .map(comment ->
                            new EventFullDto.Comment(
                                comment.getAuthor().getId(),
                                comment.getAuthor().getName(),
                                comment.getText()
                        ))
                        .collect(Collectors.toList())
        );
    }

    public static List<EventFullDto> toFullDtos(List<Event> events) {
        return events.stream().map(EventMapper::toFullDto).collect(Collectors.toList());
    }

    public static EventShortDto toShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                event.getEventDate(),
                event.getPaid(),
                new EventShortDto.Category(
                        event.getCategory().getId(),
                        event.getCategory().getName()),
                new EventShortDto.User(
                        event.getInitiator().getId(),
                        event.getInitiator().getName()),
                eventClient.getStats(
                        List.of("/events/" + event.getId())).stream()
                        .filter(stat -> stat.getUri().split("/")[2].equals(event.getId().toString()))
                        .findFirst().get().getHits(),
                eventRepository.countApprovedRequests(event.getId()),
                commentRepository.findByEventId(event.getId()).stream()
                        .map(comment ->
                                new EventShortDto.Comment(
                                        comment.getAuthor().getId(),
                                        comment.getAuthor().getName(),
                                        comment.getText()
                                ))
                        .collect(Collectors.toList()));
    }

    public static List<EventShortDto> toShortDtos(List<Event> events) {
        return events.stream().map(EventMapper::toShortDto).collect(Collectors.toList());
    }

    public static Page<EventShortDto> convertPageToShortDto(Page<Event> page) {
        if (page.isEmpty()) {
            return Page.empty();
        }

        return new PageImpl<>(toShortDtos(page.getContent()), page.getPageable(), page.getTotalElements());
    }
}
