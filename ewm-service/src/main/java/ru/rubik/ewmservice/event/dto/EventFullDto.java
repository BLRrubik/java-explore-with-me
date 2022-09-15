package ru.rubik.ewmservice.event.dto;

import lombok.*;
import ru.rubik.ewmservice.event.entity.EventState;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    private Long id;
    private String title;
    private String description;
    private String annotation;
    private LocalDateTime createdOn;
    private LocalDateTime eventDate;
    private LocalDateTime publishedOn;
    private Boolean paid;
    private EventState state;
    private Boolean requestModeration;
    private Integer participantLimit;
    private Category category;
    private User initiator;
    private Integer views = 0;
    //todo -> confirmedRequests


    public EventFullDto(Long id,
                        String title,
                        String description,
                        String annotation,
                        LocalDateTime createdOn,
                        LocalDateTime eventDate,
                        LocalDateTime publishedOn,
                        Boolean paid,
                        EventState state,
                        Boolean requestModeration,
                        Integer participantLimit,
                        Category category,
                        User initiator) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.annotation = annotation;
        this.createdOn = createdOn;
        this.eventDate = eventDate;
        this.publishedOn = publishedOn;
        this.paid = paid;
        this.state = state;
        this.requestModeration = requestModeration;
        this.participantLimit = participantLimit;
        this.category = category;
        this.initiator = initiator;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Category {
        private Long id;
        private String name;
    }
}
