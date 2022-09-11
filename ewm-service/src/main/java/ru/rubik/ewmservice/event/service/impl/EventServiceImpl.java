package ru.rubik.ewmservice.event.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import ru.rubik.ewmservice.category.exception.CategoryNotFoundException;
import ru.rubik.ewmservice.category.repository.CategoryRepository;
import ru.rubik.ewmservice.event.dto.EventFullDto;
import ru.rubik.ewmservice.event.dto.EventShortDto;
import ru.rubik.ewmservice.event.entity.Event;
import ru.rubik.ewmservice.event.entity.EventState;
import ru.rubik.ewmservice.event.exception.EventNotFoundException;
import ru.rubik.ewmservice.event.exception.EventStateException;
import ru.rubik.ewmservice.event.filter.EventFilter;
import ru.rubik.ewmservice.event.filter.EventSort;
import ru.rubik.ewmservice.event.mapper.EventMapper;
import ru.rubik.ewmservice.event.repository.EventRepository;
import ru.rubik.ewmservice.event.requests.EventAdminUpdateRequest;
import ru.rubik.ewmservice.event.requests.EventCreateRequest;
import ru.rubik.ewmservice.event.requests.EventUpdateRequest;
import ru.rubik.ewmservice.event.service.EventService;
import ru.rubik.ewmservice.event_request.dto.RequestDto;
import ru.rubik.ewmservice.event_request.entity.Request;
import ru.rubik.ewmservice.event_request.entity.RequestStatus;
import ru.rubik.ewmservice.event_request.exception.RequestNotFoundException;
import ru.rubik.ewmservice.event_request.mapper.RequestMapper;
import ru.rubik.ewmservice.event_request.repository.RequestRepository;
import ru.rubik.ewmservice.user.exception.UserNotFoundException;
import ru.rubik.ewmservice.user.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository,
                            UserRepository userRepository,
                            CategoryRepository categoryRepository,
                            RequestRepository requestRepository,
                            JdbcTemplate jdbcTemplate) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Page<EventShortDto> search(EventFilter filter, Integer from, Integer size) {
        return EventMapper.convertPageToShortDto(searchByFilters(filter, from, size));
    }

    @Override
    public Page<EventFullDto> searchByAdmin(EventFilter filter, Integer from, Integer size) {
        return EventMapper.convertPageToFullDto(searchByFilters(filter, from, size));
    }

    @Override
    public EventFullDto getEventById(Long eventId) {
        if (!eventRepository.findById(eventId).isPresent()) {
            throw new EventNotFoundException("Event with id " + eventId + " is not found");
        }

        //todo -> request to stats

        return EventMapper.toFullDto(eventRepository.findByIdAndState(eventId, EventState.PUBLISHED));
    }

    @Override
    public Page<EventShortDto> getEventsByUser(Long userId, Integer from, Integer size) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("User with id " + userId + "is not found");
        }

        Page<Event> events = eventRepository.findByInitiatorId(userId, PageRequest.of(from / size, size));

        return EventMapper.convertPageToShortDto(events);
    }

    @Override
    public EventFullDto updateEventByUser(Long userId, EventUpdateRequest request) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("User with id " + userId + "is not found");
        }

        if (!checkEventByUser(request.getEventId(), userId)) {
            throw new EventNotFoundException("User with id " + userId +
                    " dont have event with id " + request.getEventId());
        }

        if (!categoryRepository.findById(request.getCategory()).isPresent()) {
            throw new CategoryNotFoundException("Category with id " + request.getCategory() + " is not found");
        }

        if (!eventRepository.findById(request.getEventId()).isPresent()) {
            throw new EventNotFoundException("Event with id " + request.getEventId() + " is not found");
        }

        Event event = eventRepository.findById(request.getEventId()).get();

        event.setAnnotation(request.getAnnotation());
        event.setCategory(categoryRepository.findById(request.getCategory()).get());
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());
        event.setPaid(request.getPaid());
        event.setParticipantLimit(request.getParticipantLimit());
        event.setTitle(request.getTitle());

        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, EventAdminUpdateRequest request) {
        if (!categoryRepository.findById(request.getCategory()).isPresent()) {
            throw new CategoryNotFoundException("Category with id " + request.getCategory() + " is not found");
        }

        if (!eventRepository.findById(eventId).isPresent()) {
            throw new EventNotFoundException("Event with id " + eventId + " is not found");
        }

        Event event = eventRepository.findById(eventId).get();

        event.setAnnotation(request.getAnnotation());
        event.setCategory(categoryRepository.findById(request.getCategory()).get());
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());
        event.setPaid(request.getPaid());
        event.setParticipantLimit(request.getParticipantLimit());
        event.setTitle(request.getTitle());
        event.setRequestModeration(request.getRequestModeration());

        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto createEventByUser(Long userId, EventCreateRequest request) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("User with id " + userId + "is not found");
        }
        if (!categoryRepository.findById(request.getCategory()).isPresent()) {
            throw new CategoryNotFoundException("Category with id " + request.getCategory() + " is not found");
        }

        Event event = new Event();

        event.setAnnotation(request.getAnnotation());
        event.setCategory(categoryRepository.findById(request.getCategory()).get());
        event.setDescription(request.getDescription());
        event.setCreatedOn(LocalDateTime.now());
        event.setEventDate(request.getEventDate());
        event.setPaid(request.getPaid());
        event.setParticipantLimit(request.getParticipantLimit());
        event.setRequestModeration(request.getRequestModeration());
        event.setTitle(request.getTitle());
        event.setInitiator(userRepository.findById(userId).get());
        event.setState(EventState.PENDING);

        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getEventOfUserById(Long userId, Long eventId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("User with id " + userId + "is not found");
        }

        if (!checkEventByUser(eventId, userId)) {
            throw new EventNotFoundException("User with id " + userId + " dont have event with id " + eventId);
        }

        return EventMapper.toFullDto(eventRepository.findByIdAndInitiatorId(eventId, userId));
    }

    @Override
    public EventFullDto cancelEventByUser(Long userId, Long eventId, EventUpdateRequest request) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("User with id " + userId + "is not found");
        }

        if (!checkEventByUser(eventId, userId)) {
            throw new EventNotFoundException("User with id " + userId + " dont have event with id " + eventId);
        }

        if (!categoryRepository.findById(request.getCategory()).isPresent()) {
            throw new CategoryNotFoundException("Category with id " + request.getCategory() + " is not found");
        }

        if (!eventRepository.findById(eventId).isPresent()) {
            throw new EventNotFoundException("Event with id " + eventId + " is not found");
        }

        Event event = eventRepository.findById(eventId).get();

        if (!event.getState().equals(EventState.PENDING)) {
            throw new EventStateException("Event with id " + eventId + " must be PENDING state");
        }

        event.setAnnotation(request.getAnnotation());
        event.setCategory(categoryRepository.findById(request.getCategory()).get());
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());
        event.setPaid(request.getPaid());
        event.setParticipantLimit(request.getParticipantLimit());
        event.setTitle(request.getTitle());
        event.setState(EventState.CANCELED);

        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto publishEvent(Long eventId) {
        if (!eventRepository.findById(eventId).isPresent()) {
            throw new EventNotFoundException("Event with id " + eventId + " is not found");
        }

        Event event = eventRepository.findById(eventId).get();

        event.setState(EventState.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());

        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto rejectEvent(Long eventId) {
        if (!eventRepository.findById(eventId).isPresent()) {
            throw new EventNotFoundException("Event with id " + eventId + " is not found");
        }

        Event event = eventRepository.findById(eventId).get();

        event.setState(EventState.CANCELED);
        event.setPublishedOn(null);

        return EventMapper.toFullDto(eventRepository.save(event));
    }

    @Override
    public List<RequestDto> getRequestsByEvent(Long userId, Long eventId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("User with id " + userId + "is not found");
        }

        if (!eventRepository.findById(eventId).isPresent()) {
            throw new EventNotFoundException("Event with id " + eventId + " is not found");
        }

        if (!checkEventByUser(eventId, userId)) {
            throw new EventNotFoundException("User with id " + userId + " dont have event with id " + eventId);
        }

        return RequestMapper.toDtos(requestRepository.findByEventId(eventId));
    }

    @Override
    public RequestDto confirmRequest(Long userId, Long eventId, Long requestId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("User with id " + userId + " is not found");
        }

        if (!eventRepository.findById(eventId).isPresent()) {
            throw new EventNotFoundException("Event with id " + eventId + " is not found");
        }

        if (!requestRepository.findById(requestId).isPresent()) {
            throw new RequestNotFoundException("Request with id " + requestId + " is not found");
        }

        Request request = requestRepository.findById(requestId).get();

        request.setStatus(RequestStatus.APPROVED);

        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public RequestDto rejectRequest(Long userId, Long eventId, Long requestId) {
        if (!userRepository.findById(userId).isPresent()) {
            throw new UserNotFoundException("User with id " + userId + " is not found");
        }

        if (!eventRepository.findById(eventId).isPresent()) {
            throw new EventNotFoundException("Event with id " + eventId + " is not found");
        }

        if (!requestRepository.findById(requestId).isPresent()) {
            throw new RequestNotFoundException("Request with id " + requestId + " is not found");
        }

        Request request = requestRepository.findById(requestId).get();

        request.setStatus(RequestStatus.APPROVED);

        return RequestMapper.toDto(requestRepository.save(request));
    }

    private Page<Event> searchByFilters(EventFilter filter, Integer from, Integer size) {
        Set<String> params = new HashSet<>();

        params.add("limit " + size);
        params.add("offset " + from);

        Set<String> whereClauses = new HashSet<>();
        Set<String> orderClauses = new HashSet<>();


        if (filter.getOnlyAvailable() != null && filter.getOnlyAvailable()) {
            whereClauses.add(" (e.participant_limit = 0 OR" +
                    "  e.participant_limit > (select count(*) from requests as r " +
                    "where r.status like 'APPROVED' and r.event_id = e.event_id group by r.event_id) " +
                    "OR (select count(*) from requests as r " +
                    "where r.status like 'APPROVED' and r.event_id = e.event_id group by r.event_id) is null) ");
        }

        if (filter.getCategories() != null && !filter.getCategories().isEmpty()) {
            List<String> categories = filter.getCategories().stream()
                    .map(id -> "e.category_id=" + id)
                    .toList();

            whereClauses.add("(" + String.join(" or ", categories) + ")");
        }

        if (filter.getPaid() != null) {
            whereClauses.add("e.paid=" + filter.getPaid());
        }

        if (filter.getRangeStart() != null) {
            String timestamp = filter.getRangeStart().toString().split("T")[0] + " " +
                    filter.getRangeStart().toString().split("T")[1];

            whereClauses.add("date(e.event_date)>'" + timestamp + "'");
        }
        else {
            String timestamp = LocalDateTime.now().toString().split("T")[0] + " " +
                    LocalDateTime.now().toString().split("T")[1];

            whereClauses.add("date(e.event_date)>'" + timestamp + "'");
        }

        if (filter.getRangeEnd() != null) {
            String timestamp = filter.getRangeEnd().toString().split("T")[0] + " " +
                    filter.getRangeEnd().toString().split("T")[1];
            whereClauses.add("date(e.event_date)<'" + timestamp + "'");
        }

        if (filter.getUsers() != null && !filter.getUsers().isEmpty()) {
            List<String> users = filter.getUsers().stream()
                    .map(id -> "e.initiator=" + id)
                    .toList();

            whereClauses.add("(" + String.join(" or ", users) + ")");

        }

        if (filter.getStates() != null && !filter.getStates().isEmpty()) {
            filter.getStates().stream()
                    .map(state -> "e.state='" + state.toString() + "'")
                    .forEach(whereClauses::add);
        }

        if (filter.getText() != null) {
            whereClauses.add(" (e.annotation ILIKE '%" + filter.getText() + "%' " +
                    "or e.description ILIKE '%" + filter.getText() + "%') ");
        }

        if (filter.getSort() != null) {
            orderClauses.add(
              filter.getSort().equals(EventSort.EVENT_DATE) ? " e.event_date " : " e.event_id "
            );
        }

        String query = "select * from events as e " +
                " where " + String.join(" and ", whereClauses) +
                " order by " + String.join(", ", orderClauses) +
                " " + String.join(" ", params);

        System.out.println(query);

        var result = jdbcTemplate.query(query, this::mapRow);

        if (result.isEmpty()) {
            return Page.empty();
        }

        return new PageImpl<>(result, PageRequest.of(from/size, size), result.size());
    }
    private Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        Event event = new Event();
        event.setId(rs.getLong("event_id"));
        event.setTitle(rs.getString("title"));
        event.setDescription(rs.getString("description"));
        event.setAnnotation(rs.getString("annotation"));
        event.setCreatedOn(rs.getTimestamp("created_on").toLocalDateTime());
        event.setEventDate(rs.getTimestamp("event_date").toLocalDateTime());
        event.setPublishedOn(rs.getTimestamp("published_on") == null ?
                null : rs.getTimestamp("published_on").toLocalDateTime());
        event.setPaid(rs.getBoolean("paid"));
        event.setState(EventState.valueOf(rs.getString("state")));
        event.setRequestModeration(rs.getBoolean("request_moderation"));
        event.setParticipantLimit(rs.getInt("participant_limit"));
        event.setCategory(categoryRepository.findById(rs.getLong("category_id")).get());
        event.setInitiator(userRepository.findById(rs.getLong("initiator")).get());

        return event;
    }

    private boolean checkEventByUser(Long eventId, Long userId) {
        return eventRepository.findByIdAndInitiatorId(eventId, userId) != null;
    }
}
