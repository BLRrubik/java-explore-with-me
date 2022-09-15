package ru.rubik.ewmservice.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.rubik.ewmservice.event.entity.Event;
import ru.rubik.ewmservice.event.entity.EventState;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByInitiatorId(Long userId, Pageable pageable);

    Event findByIdAndInitiatorId(Long eventId, Long userId);

    Event findByIdAndState(Long eventId, EventState state);

    @Query(nativeQuery = true,
            value = "select count(*) from requests as r " +
                    "left join events as e on e.event_id = r.event_id " +
                    "where r.status like 'APPROVED' and e.event_id = ?")
    int countApprovedRequests(Long eventId);
}
