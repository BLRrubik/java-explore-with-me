package ru.rubik.ewmservice.event.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventUpdateRequest {
    private Long eventId;
    private String title;
    private String description;
    private String annotation;
    private LocalDateTime eventDate;
    private Boolean paid;
    private Long category;
    private Integer participantLimit;
}
