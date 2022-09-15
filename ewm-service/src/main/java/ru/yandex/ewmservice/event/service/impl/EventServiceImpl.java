package ru.yandex.ewmservice.event.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.ewmservice.category.exception.CategoryNotFoundException;
import ru.yandex.ewmservice.category.repository.CategoryRepository;
import ru.yandex.ewmservice.client.event.EventClient;
import ru.yandex.ewmservice.event.dto.EventFullDto;
import ru.yandex.ewmservice.event.dto.EventShortDto;
import ru.yandex.ewmservice.event.entity.Event;
import ru.yandex.ewmservice.event.entity.EventState;
import ru.yandex.ewmservice.event.exception.EventNotFoundException;
import ru.yandex.ewmservice.event.exception.EventStateException;
import ru.yandex.ewmservice.event.filter.EventFilter;
import ru.yandex.ewmservice.event.filter.EventSort;
import ru.yandex.ewmservice.event.mapper.EventMapper;
import ru.yandex.ewmservice.event.repository.EventRepository;
import ru.yandex.ewmservice.event.requests.EventAdminUpdateRequest;
import ru.yandex.ewmservice.event.requests.EventCreateRequest;
import ru.yandex.ewmservice.event.requests.EventUpdateRequest;
import ru.yandex.ewmservice.event.service.EventService;
import ru.yandex.ewmservice.event_request.dto.RequestDto;
import ru.yandex.ewmservice.event_request.entity.Request;
import ru.yandex.ewmservice.event_request.entity.RequestStatus;
import ru.yandex.ewmservice.event_request.exception.RequestNotFoundException;
import ru.yandex.ewmservice.event_request.mapper.RequestMapper;
import ru.yandex.ewmservice.event_request.repository.RequestRepository;
import ru.yandex.ewmservice.user.exception.UserNotFoundException;
import ru.yandex.ewmservice.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final JdbcTemplate jdbcTemplate;
    private final EventClient eventClient;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository,
                            UserRepository userRepository,
                            CategoryRepository categoryRepository,
                            RequestRepository requestRepository,
                            JdbcTemplate jdbcTemplate, EventClient eventClient) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.requestRepository = requestRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.eventClient = eventClient;
    }

    @Override
    public List<EventShortDto> search(EventFilter filter, Integer from, Integer size) {
        List<EventShortDto> pageDto = EventMapper.toShortDtos(searchByFilters(filter, from, size));

        if (filter.getSort().equals(EventSort.EVENT_DATE)) {
            return pageDto.stream()
                    .sorted(Comparator.comparing(EventShortDto::getEventDate))
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        }

        if (filter.getSort().equals(EventSort.VIEWS)) {
            List<EventShortDto> dtos = pageDto.stream()
                    .sorted(Comparator.comparing(EventShortDto::getViews))
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());

            Collections.reverse(dtos);

            return dtos;
        }

        return pageDto;
    }

    @Override
    public List<EventFullDto> searchByAdmin(EventFilter filter,
                                            Integer from,
                                            Integer size) {

        List<EventFullDto> pageDto = EventMapper.toFullDtos(searchByFilters(filter, from, size));


        if (filter.getSort().equals(EventSort.EVENT_DATE)) {
            return pageDto.stream()
                    .sorted(Comparator.comparing(EventFullDto::getEventDate))
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        }

        if (filter.getSort().equals(EventSort.VIEWS)) {
            return pageDto.stream()
                    .sorted(Comparator.comparing(EventFullDto::getViews))
                    .skip(from)
                    .limit(size)
                    .collect(Collectors.toList());
        }

        return pageDto;
    }

    @Override
    public EventFullDto getEventById(Long eventId, HttpServletRequest httpRequest) {
        if (!eventRepository.findById(eventId).isPresent()) {
            throw new EventNotFoundException("Event with id " + eventId + " is not found");
        }

        eventClient.hit(httpRequest);

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
        event.setLatitude(request.getLocation().getLatitude());
        event.setLongitude(request.getLocation().getLongitude());

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
        event.setLatitude(request.getLocation().getLatitude());
        event.setLongitude(request.getLocation().getLongitude());

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
    public EventFullDto cancelEventByUser(Long userId,
                                          Long eventId,
                                          EventUpdateRequest request) {

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

    private List<Event> searchByFilters(EventFilter filter, Integer from, Integer size) {

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
        } else {
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
                (!orderClauses.isEmpty() ? " order by " + String.join(", ", orderClauses) : "");


        return jdbcTemplate.query(query, this::mapRow);
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
