package com.mpt.journal.service;

import com.mpt.journal.model.CategoryModel;
import com.mpt.journal.repository.InMemoryCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private InMemoryCategoryRepository repo;

    public List<CategoryModel> getAll() {
        return repo.findAll();
    }

    public List<CategoryModel> getActive() {
        return repo.findAll().stream()
                .filter(c -> !c.isIsDeleted())
                .collect(Collectors.toList());
    }

    public CategoryModel getById(int id) {
        return repo.findById(id);
    }

    public void add(CategoryModel c) {
        repo.add(c);
    }

    public void update(CategoryModel c) {
        repo.update(c);
    }

    public void delete(int id) {
        repo.delete(id);
    }

    public boolean softDelete(int id) {
        CategoryModel category = repo.findById(id);
        if (category != null && !category.isIsDeleted()) {
            category.setIsDeleted(true);
            return true;
        }
        return false;
    }

    public void deleteMultiple(List<Integer> ids) {
        ids.forEach(this::delete);
    }

    public void softDeleteMultiple(List<Integer> ids) {
        ids.forEach(this::softDelete);
    }

    public List<CategoryModel> search(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getActive();
        }
        String term = searchTerm.toLowerCase();
        return getActive().stream()
                .filter(c -> c.getName().toLowerCase().contains(term) ||
                        c.getDescription().toLowerCase().contains(term))
                .collect(Collectors.toList());
    }

    public List<CategoryModel> getWithPagination(int page, int size, List<CategoryModel> sourceList) {
        if (sourceList == null || sourceList.isEmpty()) {
            return List.of();
        }
        int fromIndex = (page - 1) * size;
        if (fromIndex >= sourceList.size()) {
            return List.of();
        }
        int toIndex = Math.min(fromIndex + size, sourceList.size());
        return sourceList.subList(fromIndex, toIndex);
    }
}