package ru.yandex.ewmservice.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.ewmservice.category.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
