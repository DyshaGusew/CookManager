package com.example.projectcookmanager;

import DishModel.DishCard;
import com.example.projectcookmanager.DataBases.DBFavoritesRecipes;
import com.example.projectcookmanager.Entity.Recipe;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FavoriteListCardController implements Initializable {

    @FXML
    private GridPane favoriteGridContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeFavoriteDishes();
    }

    @FXML
    public void initializeFavoriteDishes() {
        favoriteGridContainer.getChildren().clear();
        List<Recipe> favoriteRecipes = new DBFavoritesRecipes().ReadAllOnFavorite();

        int column = 0;
        int row = 1;

        try {
            for (Recipe favoriteRecipe : favoriteRecipes) {
                addDishCardToGrid(favoriteRecipe, column, row);
                if (column == 1) {
                    column = 0;
                    ++row;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addDishCardToGrid(Recipe favoriteRecipe, int column, int row) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DishCardFavorite.fxml"));
        Pane dishBox = fxmlLoader.load();
        DishCardController dishCardController = fxmlLoader.getController();

        DishCard dishCard = createDishCardFromRecipe(favoriteRecipe);
        dishCardController.setData(dishCard, favoriteRecipe);

        favoriteGridContainer.add(dishBox, column++, row);
        GridPane.setMargin(dishBox, new Insets(5));
    }

    private DishCard createDishCardFromRecipe(Recipe recipe) {
        DishCard dishCard = new DishCard();
        dishCard.setName(recipe.name);
        dishCard.setTime(recipe.getTimeCooking());
        dishCard.setCalories(recipe.getCalories());
        dishCard.setImageUrl(recipe.getMainImageLink());
        dishCard.setRatingUrl(recipe.getRating());

        return dishCard;
    }
}
