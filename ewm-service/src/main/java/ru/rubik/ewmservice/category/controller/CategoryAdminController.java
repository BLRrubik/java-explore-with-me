package ru.rubik.ewmservice.category.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rubik.ewmservice.category.dto.CategoryDto;
import ru.rubik.ewmservice.category.requests.CategoryCreateRequest;
import ru.rubik.ewmservice.category.requests.CategoryUpdateRequest;
import ru.rubik.ewmservice.category.service.CategoryService;

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
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryCreateRequest request) {
        return  ResponseEntity.of(Optional.of(categoryService.createCategory(request)));
    }

    @PatchMapping
    public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryUpdateRequest request) {
        return ResponseEntity.of(Optional.of(categoryService.updateCategory(request)));
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable("catId") Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }
}
