package ru.yandex.ewmservice.category.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmservice.category.dto.CategoryDto;
import ru.yandex.ewmservice.category.requests.CategoryCreateRequest;
import ru.yandex.ewmservice.category.requests.CategoryUpdateRequest;
import ru.yandex.ewmservice.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Optional;

@RestController
@RequestMapping("/admin/categories")
public class CategoryAdminController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryAdminController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping()
    public ResponseEntity<CategoryDto> createCategory(@RequestBody @Valid CategoryCreateRequest request) {
        return ResponseEntity.of(Optional.of(categoryService.createCategory(request)));
    }

    @PatchMapping
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody @Valid CategoryUpdateRequest request) {
        return ResponseEntity.of(Optional.of(categoryService.updateCategory(request)));
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable("catId") @Positive Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}
