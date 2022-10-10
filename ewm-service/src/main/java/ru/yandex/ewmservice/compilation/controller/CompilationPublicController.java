package ru.yandex.ewmservice.compilation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmservice.compilation.dto.CompilationDto;
import ru.yandex.ewmservice.compilation.service.CompilationService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/compilations")
public class CompilationPublicController {
    private final CompilationService compilationService;

    @Autowired
    public CompilationPublicController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getAll(@RequestParam(value = "pinned", defaultValue = "false")
                                                       @NotNull  Boolean pinned,
                                                       @RequestParam(value = "from", defaultValue = "0")
                                                       @PositiveOrZero Integer from,
                                                       @RequestParam(value = "size", defaultValue = "10")
                                                       @Positive Integer size) {
        return ResponseEntity.of(Optional.of(compilationService.getAll(pinned, from, size).getContent()));
    }

    @GetMapping("/{compilationId}")
    public ResponseEntity<CompilationDto> getById(@PathVariable("compilationId") @Positive Long compilationId) {
        return ResponseEntity.of(Optional.of(compilationService.getById(compilationId)));
    }
}
