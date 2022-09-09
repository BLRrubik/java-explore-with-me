package ru.rubik.ewmservice.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    //todo -> confirmedRequests
    //todo -> views


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
