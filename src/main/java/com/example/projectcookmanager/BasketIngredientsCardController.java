package com.example.projectcookmanager;

import DishModel.BasketCard;
import com.example.projectcookmanager.DataBases.DBAllProducts;
import com.example.projectcookmanager.DataBases.DBBasketRecipes;
import com.example.projectcookmanager.DataBases.DBRecConnectProd;
import com.example.projectcookmanager.Entity.Product;
import com.example.projectcookmanager.Entity.ProductPattern;
import com.example.projectcookmanager.Entity.Recipe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class BasketIngredientsCardController implements Initializable {
    @FXML
    private GridPane basketGridContainer;

    private List<String> allIngredient = new ArrayList<>();

    private List<String> allCalories = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeBasketIngredients();
    }

    @FXML
    void showResult(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("TotalIngredientsCard.fxml"));
            Parent root = fxmlLoader.load();

            TotalIngredientsCardController totalIngredientsCardController = fxmlLoader.getController();

            Map<String, Integer> ingredientMap = getCombinedIngredients(allIngredient, allCalories);
            List<String> combinedIngredients = new ArrayList<>(ingredientMap.keySet());

            List<String> combinedCalories = ingredientMap.values().stream()
                    .map(calories -> calories + " гр")
                    .collect(Collectors.toList());

            totalIngredientsCardController.SetData(combinedIngredients, combinedCalories);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Integer> getCombinedIngredients(List<String> ingredients, List<String> calories) {
        Map<String, Integer> ingredientMap = new HashMap<>();

        for (int i = 0; i < ingredients.size(); i++) {
            String ingredient = ingredients.get(i);
            double calorie = Double.parseDouble(calories.get(i).replace(" гр", ""));

            int calorieInt = (int) calorie;

            if (ingredientMap.containsKey(ingredient)) {
                int totalCalorie = ingredientMap.get(ingredient) + calorieInt;
                ingredientMap.put(ingredient, totalCalorie);
            }
            else {
                ingredientMap.put(ingredient, calorieInt);
            }
        }

        return ingredientMap;
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

                allIngredient.addAll(basketCard.getListOfIngredients());
                allCalories.addAll(basketCard.getListOfCalories());

                basketCardController.setData(basketCard);

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