package ru.yandex.ewmservice.event_request.service;

import ru.yandex.ewmservice.event_request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    List<RequestDto> getAllByUser(Long userId);

    RequestDto createRequest(Long userId, Long eventId);

    RequestDto cancelRequestByUser(Long userId, Long requestId);
}
