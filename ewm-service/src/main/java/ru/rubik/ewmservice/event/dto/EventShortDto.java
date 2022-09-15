package ru.rubik.ewmservice.event.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    private Long id;
    private String title;
    private String annotation;
    private LocalDateTime eventDate;
    private Boolean paid;
    private Category category;
    private User initiator;
    private Integer views = 0;
    //todo -> confirmedRequests


    public EventShortDto(Long id,
                         String title,
                         String annotation,
                         LocalDateTime eventDate,
                         Boolean paid,
                         Category category,
                         User initiator) {
        this.id = id;
        this.title = title;
        this.annotation = annotation;
        this.eventDate = eventDate;
        this.paid = paid;
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
