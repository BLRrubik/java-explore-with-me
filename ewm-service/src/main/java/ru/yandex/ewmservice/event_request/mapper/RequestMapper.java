package ru.yandex.ewmservice.event_request.mapper;

import ru.yandex.ewmservice.event_request.dto.RequestDto;
import ru.yandex.ewmservice.event_request.entity.Request;

import java.util.List;
import java.util.stream.Collectors;

public class RequestMapper {
    public static RequestDto toDto(Request request) {
        return new RequestDto(
                request.getId(),
                request.getStatus(),
                request.getCreated(),
                request.getRequester().getId(),
                request.getEvent().getId()
        );
    }

    public static List<RequestDto> toDtos(List<Request> requests) {
        return requests.stream()
                .map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }
}
