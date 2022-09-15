package ru.yandex.ewmservice.category.service;

import org.springframework.data.domain.Page;
import ru.yandex.ewmservice.category.dto.CategoryDto;
import ru.yandex.ewmservice.category.requests.CategoryCreateRequest;
import ru.yandex.ewmservice.category.requests.CategoryUpdateRequest;

public interface CategoryService {
    Page<CategoryDto> getAll(Integer from, Integer size);

    CategoryDto getById(Long categoryId);

    CategoryDto createCategory(CategoryCreateRequest request);

    CategoryDto updateCategory(CategoryUpdateRequest request);

    void deleteCategory(Long categoryId);
}
