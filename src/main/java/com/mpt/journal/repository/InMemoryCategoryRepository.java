package com.mpt.journal.repository;

import com.mpt.journal.model.CategoryModel;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryCategoryRepository {
    private List<CategoryModel> categories = new ArrayList<>();
    private AtomicInteger idCounter = new AtomicInteger(1);

    public List<CategoryModel> findAll() {
        return new ArrayList<>(categories);
    }

    public CategoryModel findById(int id) {
        return categories.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public CategoryModel add(CategoryModel category) {
        category.setId(idCounter.getAndIncrement());
        categories.add(category);
        return category;
    }

    public CategoryModel update(CategoryModel category) {
        CategoryModel existing = findById(category.getId());
        if (existing != null) {
            existing.setName(category.getName());
            existing.setDescription(category.getDescription());
        }
        return existing;
    }

    public void delete(int id) {
        categories.removeIf(c -> c.getId() == id);
    }
}