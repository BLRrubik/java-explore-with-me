package ru.rubik.ewmservice.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.rubik.ewmservice.event.entity.Event;
import ru.rubik.ewmservice.event.entity.EventState;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByInitiatorId(Long userId, Pageable pageable);

    Event findByIdAndInitiatorId(Long eventId, Long userId);

    Event findByIdAndState(Long eventId, EventState state);
}
