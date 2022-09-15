package ru.yandex.ewmservice.category.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.yandex.ewmservice.category.dto.CategoryDto;
import ru.yandex.ewmservice.category.entity.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {
    public static CategoryDto toDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName()
        );
    }

    public static List<CategoryDto> toDtos(List<Category> categories) {
        return categories.stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    public static Page<CategoryDto> convertPageToDto(Page<Category> page) {
        if (page.isEmpty()) {
            return Page.empty();
        }

        return new PageImpl<>(toDtos(page.getContent()), page.getPageable(), page.getTotalElements());
    }

}
