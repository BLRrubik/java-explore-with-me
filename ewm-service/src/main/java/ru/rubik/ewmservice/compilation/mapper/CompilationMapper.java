package ru.rubik.ewmservice.compilation.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.rubik.ewmservice.compilation.dto.CompilationDto;
import ru.rubik.ewmservice.compilation.entity.Compilation;
import ru.rubik.ewmservice.event.entity.Event;
import ru.rubik.ewmservice.event.mapper.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static CompilationDto toDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getId(),
                compilation.getTitle(),
                compilation.getPinned(),
                compilation.getEvents().stream()
                        .map(EventMapper::toShortDto)
                        .collect(Collectors.toList())
        );
    }

    public static List<CompilationDto> toDtos(List<Compilation> compilations) {
        return compilations.stream()
                .map(CompilationMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Page<CompilationDto> convertPageToDto(Page<Compilation> page) {
        if (page.isEmpty())
        {
            return Page.empty();
        }

        return new PageImpl<>(toDtos(page.getContent()), page.getPageable(), page.getTotalElements());
    }
}
