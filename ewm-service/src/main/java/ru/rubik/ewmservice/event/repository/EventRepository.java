package ru.rubik.ewmservice.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rubik.ewmservice.event.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
