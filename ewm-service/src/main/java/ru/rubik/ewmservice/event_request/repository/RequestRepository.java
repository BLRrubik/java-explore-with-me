package ru.rubik.ewmservice.event_request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rubik.ewmservice.event_request.entity.Request;
import ru.rubik.ewmservice.event_request.entity.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequesterId(Long userId);

    Request findByRequesterIdAndEventId(Long userId, Long eventId);

    List<Request> findByEventIdAndStatus(Long eventId, RequestStatus status);

    List<Request> findByEventId(Long eventId);
}
