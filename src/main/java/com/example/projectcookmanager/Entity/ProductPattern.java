package com.example.projectcookmanager.Entity;

//Шаблоны продуктов
public class ProductPattern extends Entity {
    private float protein;
    private float fat;
    private float carbohydrate;

    public void setProtein(float protein){
        this.protein = protein;
    }
    public float getProtein(){return protein;}

    public void setFat(float fat){
        this.fat = fat;
    }
    public float getFat(){return fat;}

    public void setCarbohydrate(float carbohydrate){
        this.carbohydrate = carbohydrate;
    }
    public float getCarbohydrate(){return carbohydrate;}

    public ProductPattern(int id, String name, float protein, float fat, float carbohydrate){
        this.id = id;
        this.name = name;
        setProtein(protein);
        setFat(fat);
        setCarbohydrate(carbohydrate);
    }

    public ProductPattern(String name, float protein, float fat, float carbohydrate){
        this.name = name;
        setProtein(protein);
        setFat(fat);
        setCarbohydrate(carbohydrate);
    }


}
