package com.example.projectcookmanager;

import com.example.projectcookmanager.DataBases.DBAllProducts;

import java.util.List;

public class Product extends Entity {
    public int id;
    public String name;
    public int protein;
    public int fat;
    public int carbohydrate;

    public Product(int id, String name, int protein, int fat, int carbohydrate){
        this.id = id;
        this.name = name;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrate = carbohydrate;
    }

    public Product(String name, int protein, int fat, int carbohydrate){
        this.name = name;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrate = carbohydrate;
    }


}
