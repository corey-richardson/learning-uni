package com.example.productuserssqlite;

public class Product {
    private int id;
    private String productName;
    private double price;
    private int stock;

    // Constructor
    public Product(int id, String productName, double price, int stock) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }
}
