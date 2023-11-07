package com.example.projectcookmanager.Entity;

//Обычный продукт из рецепта
public class Product extends ProductPattern{
    //Масса в грамах
    private float mass;

    public void setMass(float mass) {
        this.mass = mass;
    }

    public float getMass() {
        return mass;
    }

    public Product(int id, String name, float protein, float fat, float carbohydrate, float mass){
        super(id, name, protein, fat, carbohydrate);
        setMass(mass);
    }

    public Product(String name, float protein, float fat, float carbohydrate, float mass){
        super(name, protein, fat, carbohydrate);
        setMass(mass);
    }
}
