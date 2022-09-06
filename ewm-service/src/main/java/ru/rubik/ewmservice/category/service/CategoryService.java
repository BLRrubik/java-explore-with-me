package ru.rubik.ewmservice.category.service;

import org.springframework.data.domain.Page;
import ru.rubik.ewmservice.category.dto.CategoryDto;
import ru.rubik.ewmservice.category.requests.CategoryCreateRequest;
import ru.rubik.ewmservice.category.requests.CategoryUpdateRequest;

public interface CategoryService {
    Page<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto getById(Long categoryId);

    CategoryDto createCategory(CategoryCreateRequest request);

    CategoryDto updateCategory(CategoryUpdateRequest request);

    void deleteCategory(Long categoryId);
}
