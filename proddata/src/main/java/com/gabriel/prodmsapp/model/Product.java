package com.gabriel.prodmsapp.model;

import lombok.Data;

@Data
public class Product {
    int id;
    String name;
    String description;

    @Override
    public String toString() {
        return name;
    }
}
