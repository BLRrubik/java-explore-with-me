package ru.yandex.ewmservice.compilation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmservice.compilation.dto.CompilationDto;
import ru.yandex.ewmservice.compilation.requests.CompilationCreateRequest;
import ru.yandex.ewmservice.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Optional;

@RestController
@RequestMapping("/admin/compilations")
public class CompilationAdminController {
    private final CompilationService compilationService;

    @Autowired
    public CompilationAdminController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    public ResponseEntity<CompilationDto> createCompilation(@RequestBody @Valid CompilationCreateRequest request) {
        return ResponseEntity.of(Optional.of(compilationService.createCompilation(request)));
    }

    @DeleteMapping("/{compilationId}")
    public void deleteCompilation(@PathVariable("compilationId") @Positive Long compilationId) {
        compilationService.deleteCompilation(compilationId);
    }

    @DeleteMapping("/{compilationId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable("compilationId") @Positive Long compilationId,
                                           @PathVariable("eventId") @Positive Long eventId) {
        compilationService.deleteEventFromCompilation(compilationId, eventId);
    }

    @PatchMapping("/{compilationId}/events/{eventId}")
    public ResponseEntity<CompilationDto> addEventToCompilation(@PathVariable("compilationId") @Positive
                                                                    Long compilationId,
                                                                @PathVariable("eventId") @Positive Long eventId) {
        return ResponseEntity.of(Optional.of(compilationService.addEventFromCompilation(compilationId, eventId)));
    }

    @DeleteMapping("/{compilationId}/pin")
    public void unpinCompilation(@PathVariable("compilationId") @Positive Long compilationId) {
        compilationService.unpinCompilation(compilationId);
    }

    @PatchMapping("/{compilationId}/pin")
    public ResponseEntity<CompilationDto> pinCompilation(@PathVariable("compilationId") @Positive Long compilationId) {
        return ResponseEntity.of(Optional.of(compilationService.pinCompilation(compilationId)));
    }
}
