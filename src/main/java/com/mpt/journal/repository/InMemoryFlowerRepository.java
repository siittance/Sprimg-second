package com.mpt.journal.repository;

import com.mpt.journal.model.FlowerModel;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryFlowerRepository {
    private final List<FlowerModel> flowers = new ArrayList<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    public FlowerModel add(FlowerModel flower) {
        flower.setId(idCounter.getAndIncrement());
        flowers.add(flower);
        return flower;
    }

    public FlowerModel update(FlowerModel flower) {
        return flowers.stream()
                .filter(f -> f.getId() == flower.getId())
                .findFirst()
                .map(f -> {
                    f.setName(flower.getName());
                    f.setPrice(flower.getPrice());
                    f.setStockQuantity(flower.getStockQuantity());
                    f.setCategoryId(flower.getCategoryId());
                    return f;
                })
                .orElse(null);
    }

    public void deletePhysical(int id) {
        flowers.removeIf(flower -> flower.getId() == id);
    }

    public void deleteLogical(int id) {
        flowers.stream()
                .filter(flower -> flower.getId() == id && flower.isActive())
                .findFirst()
                .ifPresent(flower -> flower.setActive(false));
    }

    public List<FlowerModel> findAll() {
        return new ArrayList<>(flowers);
    }

    public List<FlowerModel> findActiveFlowers() {
        return flowers.stream()
                .filter(FlowerModel::isActive)
                .collect(Collectors.toList());
    }

    public FlowerModel findById(int id) {
        return flowers.stream()
                .filter(flower -> flower.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
