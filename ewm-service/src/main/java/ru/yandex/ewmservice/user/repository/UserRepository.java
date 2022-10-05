package ru.yandex.ewmservice.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.ewmservice.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(nativeQuery = true,
    value = "select * from users as u " +
            "left join requests r on u.user_id = r.requester " +
            "left join events as e on r.event_id = e.event_id " +
            "where u.user_id = ? and e.event_id = ? " +
            "  and r.status = 'APPROVED' " +
            "  and e.event_date < now()")
    Optional<User> findVisitedByEvent(Long userId, Long eventId);
}
