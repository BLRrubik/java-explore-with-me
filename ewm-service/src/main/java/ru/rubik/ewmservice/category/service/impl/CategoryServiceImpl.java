package ru.rubik.ewmservice.category.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.rubik.ewmservice.category.dto.CategoryDto;
import ru.rubik.ewmservice.category.entity.Category;
import ru.rubik.ewmservice.category.exception.CategoryNotFoundException;
import ru.rubik.ewmservice.category.exception.CategoryUniqException;
import ru.rubik.ewmservice.category.mapper.CategoryMapper;
import ru.rubik.ewmservice.category.repository.CategoryRepository;
import ru.rubik.ewmservice.category.requests.CategoryCreateRequest;
import ru.rubik.ewmservice.category.requests.CategoryUpdateRequest;
import ru.rubik.ewmservice.category.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<CategoryDto> getAll(Integer from, Integer size) {
        return CategoryMapper.convertPageToDto(
                categoryRepository.findAll(
                        PageRequest.of(from / size, size)
                )
        );
    }

    @Override
    public CategoryDto getById(Long categoryId) {
        if (!categoryRepository.findById(categoryId).isPresent()) {
            throw new CategoryNotFoundException("Category with id " + categoryId + " not found");
        }

        return CategoryMapper.toDto(categoryRepository.findById(categoryId).get());
    }

    @Override
    public CategoryDto createCategory(CategoryCreateRequest request) {
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new CategoryUniqException("Category with name " + request.getName() + " already exists");
        }

        Category category = new Category();

        category.setName(request.getName());

        return CategoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto updateCategory(CategoryUpdateRequest request) {
        if (!categoryRepository.findById(request.getId()).isPresent()) {
            throw new CategoryNotFoundException("Category with id " + request.getId() + " not found");
        }

        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new CategoryUniqException("Category with name " + request.getName() + " already exists");
        }

        Category category = categoryRepository.findById(request.getId()).get();

        category.setName(request.getName());

        return CategoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.findById(categoryId).isPresent()) {
            throw new CategoryNotFoundException("Category with id " + categoryId + " not found");
        }

        categoryRepository.deleteById(categoryId);
    }
}

