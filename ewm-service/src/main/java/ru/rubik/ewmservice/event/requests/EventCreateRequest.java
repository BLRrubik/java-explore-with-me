package ru.rubik.ewmservice.event.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventCreateRequest {
    private String title;
    private String description;
    private String annotation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Boolean paid;
    private Long category;
    private Integer participantLimit;
    private Boolean requestModeration;
    private Location location;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Location {
        private float latitude;
        private float longitude;
    }
}
