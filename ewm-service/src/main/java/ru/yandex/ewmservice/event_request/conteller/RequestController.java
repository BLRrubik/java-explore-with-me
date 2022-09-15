package ru.yandex.ewmservice.event_request.conteller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmservice.event_request.dto.RequestDto;
import ru.yandex.ewmservice.event_request.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users/{userId}/requests")
public class RequestController {
    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public ResponseEntity<List<RequestDto>> getAllByUser(@PathVariable("userId") @Positive Long userId) {
        return ResponseEntity.of(Optional.of(requestService.getAllByUser(userId)));
    }

    @PostMapping
    public ResponseEntity<RequestDto> createRequest(@PathVariable("userId") @Positive Long userId,
                                                    @RequestParam("eventId") @Positive Long eventId) {
        return ResponseEntity.of(Optional.of(requestService.createRequest(userId, eventId)));
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<RequestDto> cancelRequestByUser(@PathVariable("userId") @Positive Long userId,
                                                          @PathVariable("requestId") @Positive Long requestId) {
        return ResponseEntity.of(Optional.of(requestService.cancelRequestByUser(userId, requestId)));
    }
}
