package ru.yandex.ewmservice.category.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmservice.category.dto.CategoryDto;
import ru.yandex.ewmservice.category.service.CategoryService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryPublicController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryPublicController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<List<CategoryDto>> getAll(@RequestParam(value = "from", defaultValue = "0") Integer from,
                                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {

        return ResponseEntity.of(Optional.of(categoryService.getAll(from, size).getContent()));
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getById(@PathVariable("catId") Long categoryId) {
        return ResponseEntity.of(Optional.of(categoryService.getById(categoryId)));
    }
}
