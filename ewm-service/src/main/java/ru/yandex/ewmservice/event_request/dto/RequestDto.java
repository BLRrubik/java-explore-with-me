package ru.yandex.ewmservice.event_request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.ewmservice.event_request.entity.RequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {
    private Long id;
    private RequestStatus status;
    private LocalDateTime created;
    private Long requester;
    private Long event;
}
