package com.example.projectcookmanager.DishModel;

import java.util.List;

public class BasketCard {
    private  String dishName;
    private List<String> listOfIngredients;

    private List<String> listOfCalories;

    public List<String> getListOfCalories() {
        return listOfCalories;
    }

    public void setListOfCalories(List<String> listOfCalories) {
        this.listOfCalories = listOfCalories;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public List<String> getListOfIngredients() {
        return listOfIngredients;
    }

    public void setListOfIngredients(List<String> listOfIngredients) {
        this.listOfIngredients = listOfIngredients;
    }
}
