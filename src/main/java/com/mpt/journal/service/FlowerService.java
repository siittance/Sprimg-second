package com.mpt.journal.service;

import com.mpt.journal.model.FlowerModel;
import com.mpt.journal.model.CategoryModel;
import com.mpt.journal.repository.InMemoryFlowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlowerService {
    @Autowired
    private InMemoryFlowerRepository repo;

    @Autowired
    private CategoryService categoryService;

    public List<FlowerModel> getAll() {
        return repo.findAll();
    }

    public List<FlowerModel> findActiveFlowers() {
        return repo.findActiveFlowers();
    }

    public FlowerModel getById(int id) {
        return repo.findById(id);
    }

    public void add(FlowerModel flower) {
        repo.add(flower);
    }

    public void update(FlowerModel flower) {
        repo.update(flower);
    }

    public void deletePhysical(int id) {
        repo.deletePhysical(id);
    }

    public void deleteLogical(int id) {
        repo.deleteLogical(id);
    }

    public List<CategoryModel> getAllCategories() {
        return categoryService.getActive();
    }

    public String getCategoryNameById(int categoryId) {
        CategoryModel category = categoryService.getById(categoryId);
        return category != null ? category.getName() : "Неизвестно";
    }

    public void deleteMultiple(List<Integer> ids) {
        if (ids != null && !ids.isEmpty()) {
            ids.forEach(repo::deletePhysical);
        }
    }

    public void deleteLogicalMultiple(List<Integer> ids) {
        if (ids != null && !ids.isEmpty()) {
            ids.forEach(repo::deleteLogical);
        }
    }

    public List<FlowerModel> searchAndFilter(String search, Double minPrice, Double maxPrice, Integer categoryId) {
        return repo.findActiveFlowers().stream()
                .filter(f -> search == null || f.getName().toLowerCase().contains(search.toLowerCase()))
                .filter(f -> minPrice == null || f.getPrice() >= minPrice)
                .filter(f -> maxPrice == null || f.getPrice() <= maxPrice)
                .filter(f -> categoryId == null || f.getCategoryId() == categoryId)
                .collect(Collectors.toList());
    }

    public List<FlowerModel> getWithPagination(int page, int size, List<FlowerModel> source) {
        int fromIndex = (page - 1) * size;
        if (fromIndex >= source.size()) return List.of();
        int toIndex = Math.min(fromIndex + size, source.size());
        return source.subList(fromIndex, toIndex);
    }


}
