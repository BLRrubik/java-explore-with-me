package ru.rubik.ewmservice.compilation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rubik.ewmservice.compilation.dto.CompilationDto;
import ru.rubik.ewmservice.compilation.requests.CompilationCreateRequest;
import ru.rubik.ewmservice.compilation.service.CompilationService;

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
    public ResponseEntity<CompilationDto> createCompilation(@RequestBody CompilationCreateRequest request) {
        return ResponseEntity.of(Optional.of(compilationService.createCompilation(request)));
    }

    @DeleteMapping("/{compilationId}")
    public void deleteCompilation(@PathVariable("compilationId") Long compilationId) {
        compilationService.deleteCompilation(compilationId);
    }

    @DeleteMapping("/{compilationId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable("compilationId") Long compilationId,
                                           @PathVariable("eventId") Long eventId) {
        compilationService.deleteEventFromCompilation(compilationId, eventId);
    }

    @PatchMapping("/{compilationId}/events/{eventId}")
    public ResponseEntity<CompilationDto> addEventToCompilation(@PathVariable("compilationId") Long compilationId,
                                                                @PathVariable("eventId") Long eventId) {
        return ResponseEntity.of(Optional.of(compilationService.addEventFromCompilation(compilationId, eventId)));
    }

    @DeleteMapping("/{compilationId}/pin")
    public void unpinCompilation(@PathVariable("compilationId") Long compilationId) {
        compilationService.unpinCompilation(compilationId);
    }

    @PatchMapping("/{compilationId}/pin")
    public ResponseEntity<CompilationDto> pinCompilation(@PathVariable("compilationId") Long compilationId) {
        return ResponseEntity.of(Optional.of(compilationService.pinCompilation(compilationId)));
    }
}
