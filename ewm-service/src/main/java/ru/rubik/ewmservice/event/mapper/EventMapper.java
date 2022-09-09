package ru.rubik.ewmservice.event.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.rubik.ewmservice.category.dto.CategoryDto;
import ru.rubik.ewmservice.category.entity.Category;
import ru.rubik.ewmservice.event.dto.EventFullDto;
import ru.rubik.ewmservice.event.dto.EventShortDto;
import ru.rubik.ewmservice.event.entity.Event;

import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {
    public static EventFullDto toFullDto(Event event) {
        return new EventFullDto(
                event.getId(),
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
                        event.getCategory().getName()
                ),
                new EventFullDto.User(
                        event.getInitiator().getId(),
                        event.getInitiator().getName()
                )
                //todo -> confirmedRequests
                //todo -> views
        );
    }

    public static List<EventFullDto> toFullDtos(List<Event> events) {
        return events.stream()
                .map(EventMapper::toFullDto)
                .collect(Collectors.toList());
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
                        event.getCategory().getName()
                ),
                new EventShortDto.User(
                        event.getInitiator().getId(),
                        event.getInitiator().getName()
                )
                //todo -> confirmedRequests
                //todo -> views
        );
    }

    public static List<EventShortDto> toShortDtos(List<Event> events) {
        return events.stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    public static Page<EventFullDto> convertPageToFullDto(Page<Event> page) {
        if (page.isEmpty())
        {
            return Page.empty();
        }

        return new PageImpl<>(toFullDtos(page.getContent()), page.getPageable(), page.getTotalElements());
    }

    public static Page<EventShortDto> convertPageToShortDto(Page<Event> page) {
        if (page.isEmpty())
        {
            return Page.empty();
        }

        return new PageImpl<>(toShortDtos(page.getContent()), page.getPageable(), page.getTotalElements());
    }
}
