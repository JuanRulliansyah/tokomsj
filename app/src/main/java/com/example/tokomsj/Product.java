package com.example.tokomsj;

public class Product {
    private int id;
    private String name;
    private int price;
    private int stock;

    public Product()
    {
    }

    public Product(int id, String name, int price, int stock)
    {
        this.id = id;
        this.name=name;
        this.price = price;
        this.stock = stock;
    }

    public Product(String name, int price, int stock)
    {
        this.name=name;
        this.price=price;
        this.stock=stock;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }


    public String getName() {
        return name;
    }
}
