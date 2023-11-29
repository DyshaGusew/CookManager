package com.example.projectcookmanager.DishModel;

import javafx.scene.control.ListView;

public class FullReceiptCard {
    private String nameDish;
    private String imageUrl;

    private ListView<?> ingredients;

    private String recipeDescription;

    private String stepsDescription;

    private String rating;

    private String dishTime;

    private String dishCalories;

    private String dishCategory;

    public void dishCategory(String dishCategory) {
        this.dishCategory = dishCategory;
    }

    public void setStepsDescription(String stepsDescription) {
        this.stepsDescription = stepsDescription;
    }

    public void dishCalories(String dishCalories) {
        this.dishCalories = dishCalories;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setDishTime(String dishTime) {
        this.dishTime = dishTime;
    }

    public void setIngredients(ListView<?> ingredients) {
        this.ingredients = ingredients;
    }

    public void setNameDish(String nameDish) {
        this.nameDish = nameDish;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }
}
