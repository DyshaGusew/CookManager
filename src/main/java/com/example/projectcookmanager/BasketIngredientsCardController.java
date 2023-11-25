package com.example.projectcookmanager;

import DishModel.BasketCard;
import com.example.projectcookmanager.DataBases.*;
import com.example.projectcookmanager.Entity.Product;
import com.example.projectcookmanager.Entity.ProductPattern;
import com.example.projectcookmanager.Entity.Recipe;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BasketIngredientsCardController implements Initializable {
    @FXML
    private GridPane basketGridContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeBasketIngredients();
    }

    private void initializeBasketIngredients() {
        List<Recipe> basketRecipes = new DBBasketRecipes().ReadAllOnBasket();
        List<ProductPattern> allIngredients = new DBAllProducts().ReadAll();

        int column = 0;
        int row = 1;

        try {
            for (Recipe basketRecipe : basketRecipes) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("BasketCard.fxml"));
                Pane basketCardPane = fxmlLoader.load();

                BasketCardController basketCardController = fxmlLoader.getController();

                BasketCard basketCard = new BasketCard();
                basketCard.setDishName(basketRecipe.name);
                basketCard.setListOfIngredients(getIngredientsNames(basketRecipe, allIngredients));
                basketCard.setListOfCalories(getCalories(basketRecipe));

                basketCardController.SetData(basketCard);

                if (column == 1) {
                    column = 0;
                    ++row;
                }

                basketGridContainer.add(basketCardPane, column++, row);
                GridPane.setMargin(basketCardPane, new Insets(5));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getCalories(Recipe recipe) {
        List<Product> recipeProducts = new DBRecConnectProd().ReadAllOfRecipe(recipe.id);
        List<String> caloriesList = new ArrayList<>();
        String totalCalories;

        for (Product product : recipeProducts) {
            totalCalories = product.getMass() + " гр";
            caloriesList.add(totalCalories);
        }

        return caloriesList;
    }

    private List<String> getIngredientsNames(Recipe recipe, List<ProductPattern> allIngredients) {
        List<String> ingredientNames = new ArrayList<>();

        List<Product> recipeIngredients = new DBRecConnectProd().ReadAllOfRecipe(recipe.id);

        for (Product recipeIngredient : recipeIngredients) {
            for (ProductPattern allIngredient : allIngredients) {
                if (allIngredient.id == recipeIngredient.id) {
                    ingredientNames.add(allIngredient.name);
                    break;
                }
            }
        }

        return ingredientNames;
    }
}
