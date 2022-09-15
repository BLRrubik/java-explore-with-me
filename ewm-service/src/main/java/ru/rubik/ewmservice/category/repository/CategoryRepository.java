package ru.rubik.ewmservice.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rubik.ewmservice.category.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
