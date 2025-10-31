package com.mpt.journal.model;

public class FlowerModel {
    private int id;
    private String name;
    private double price;
    private int stockQuantity;
    private int categoryId;
    private boolean isActive;

    public FlowerModel() {
        this.isActive = true;
    }

    public FlowerModel(int id, String name, double price, int stockQuantity, int categoryId) {
        this();
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.categoryId = categoryId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
