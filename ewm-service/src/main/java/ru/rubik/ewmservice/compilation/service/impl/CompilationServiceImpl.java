package ru.rubik.ewmservice.compilation.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.rubik.ewmservice.category.exception.CategoryNotFoundException;
import ru.rubik.ewmservice.compilation.dto.CompilationDto;
import ru.rubik.ewmservice.compilation.entity.Compilation;
import ru.rubik.ewmservice.compilation.mapper.CompilationMapper;
import ru.rubik.ewmservice.compilation.repository.CompilationRepository;
import ru.rubik.ewmservice.compilation.requests.CompilationCreateRequest;
import ru.rubik.ewmservice.compilation.service.CompilationService;
import ru.rubik.ewmservice.event.entity.Event;
import ru.rubik.ewmservice.event.exception.EventNotFoundException;
import ru.rubik.ewmservice.event.repository.EventRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Page<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {

        return CompilationMapper.convertPageToDto(compilationRepository.findAllByPinned(
                pinned,
                PageRequest.of(from / size, size)
        ));
    }

    @Override
    public CompilationDto getById(Long compilationId) {
        if (!compilationRepository.findById(compilationId).isPresent()) {
            throw new CategoryNotFoundException("Compilation with id " + compilationId + " is not found");
        }

        return CompilationMapper.toDto(compilationRepository.findById(compilationId).get());
    }

    @Override
    public CompilationDto createCompilation(CompilationCreateRequest request) {
        Compilation compilation = new Compilation();

        compilation.setTitle(request.getTitle());
        compilation.setPinned(request.getPinned());
        compilation.setEvents(request.getEvents().stream()
                .map(eventRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList()));

        return CompilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Long compilationId) {
        if (!compilationRepository.findById(compilationId).isPresent()) {
            throw new CategoryNotFoundException("Compilation with id " + compilationId + " is not found");
        }

        compilationRepository.deleteById(compilationId);
    }

    @Override
    public void deleteEventFromCompilation(Long compilationId, Long eventId) {
        if (!compilationRepository.findById(compilationId).isPresent()) {
            throw new CategoryNotFoundException("Compilation with id " + compilationId + " is not found");
        }

        if (!eventRepository.findById(eventId).isPresent()) {
            throw new EventNotFoundException("Event with id " + eventId + " is not found");
        }

        Event event = eventRepository.findById(eventId).get();
        Compilation compilation = compilationRepository.findById(compilationId).get();

        compilation.getEvents().remove(event);

        compilationRepository.save(compilation);
    }

    @Override
    public CompilationDto addEventFromCompilation(Long compilationId, Long eventId) {
        if (!compilationRepository.findById(compilationId).isPresent()) {
            throw new CategoryNotFoundException("Compilation with id " + compilationId + " is not found");
        }

        if (!eventRepository.findById(eventId).isPresent()) {
            throw new EventNotFoundException("Event with id " + eventId + " is not found");
        }

        Event event = eventRepository.findById(eventId).get();
        Compilation compilation = compilationRepository.findById(compilationId).get();

        compilation.getEvents().add(event);

        return CompilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public void unpinCompilation(Long compilationId) {
        if (!compilationRepository.findById(compilationId).isPresent()) {
            throw new CategoryNotFoundException("Compilation with id " + compilationId + " is not found");
        }

        Compilation compilation = compilationRepository.findById(compilationId).get();

        compilation.setPinned(false);

        compilationRepository.save(compilation);

    }

    @Override
    public CompilationDto pinCompilation(Long compilationId) {
        if (!compilationRepository.findById(compilationId).isPresent()) {
            throw new CategoryNotFoundException("Compilation with id " + compilationId + " is not found");
        }

        Compilation compilation = compilationRepository.findById(compilationId).get();

        compilation.setPinned(true);

        return CompilationMapper.toDto(compilationRepository.save(compilation));
    }
}
