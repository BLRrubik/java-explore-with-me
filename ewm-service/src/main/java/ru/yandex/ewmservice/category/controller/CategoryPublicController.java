package ru.yandex.ewmservice.category.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmservice.category.dto.CategoryDto;
import ru.yandex.ewmservice.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
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
    public ResponseEntity<List<CategoryDto>> getAll(@RequestParam(value = "from", defaultValue = "0")
                                                        @PositiveOrZero Integer from,
                                                    @RequestParam(value = "size", defaultValue = "10")
                                                        @Positive Integer size) {

        return ResponseEntity.of(Optional.of(categoryService.getAll(from, size).getContent()));
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getById(@PathVariable("catId") @Positive Long categoryId) {
        return ResponseEntity.of(Optional.of(categoryService.getById(categoryId)));
    }
}
