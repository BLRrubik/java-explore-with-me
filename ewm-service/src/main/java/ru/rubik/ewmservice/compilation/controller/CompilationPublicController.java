package ru.rubik.ewmservice.compilation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rubik.ewmservice.compilation.dto.CompilationDto;
import ru.rubik.ewmservice.compilation.service.CompilationService;

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
                                                       Boolean pinned,
                                                       @RequestParam(value = "from", defaultValue = "0")
                                                       Integer from,
                                                       @RequestParam(value = "size", defaultValue = "10")
                                                       Integer size) {
        return ResponseEntity.of(Optional.of(compilationService.getAll(pinned, from, size).getContent()));
    }

    @GetMapping("/{compilationId}")
    public ResponseEntity<CompilationDto> getById(@PathVariable("compilationId") Long compilationId) {
        return ResponseEntity.of(Optional.of(compilationService.getById(compilationId)));
    }
}
