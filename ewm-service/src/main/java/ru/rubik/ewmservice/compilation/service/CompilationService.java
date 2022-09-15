package ru.rubik.ewmservice.compilation.service;

import org.springframework.data.domain.Page;
import ru.rubik.ewmservice.compilation.dto.CompilationDto;
import ru.rubik.ewmservice.compilation.requests.CompilationCreateRequest;

public interface CompilationService {
    Page<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);

    CompilationDto getById(Long compilationId);

    CompilationDto createCompilation(CompilationCreateRequest request);

    void deleteCompilation(Long compilationId);

    void deleteEventFromCompilation(Long compilationId, Long eventId);

    CompilationDto addEventFromCompilation(Long compilationId, Long eventId);

    void unpinCompilation(Long compilationId);

    CompilationDto pinCompilation(Long compilationId);
}
