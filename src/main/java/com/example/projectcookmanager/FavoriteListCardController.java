package com.example.projectcookmanager;

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

    private void initializeFavoriteDishes() {
        List<Recipe> favoriteRecipes = new DBFavoritesRecipes().ReadAllOnFavorite();

        int column = 0;
        int row = 1;

        try {
            for (Recipe favoriteRecipe : favoriteRecipes) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("DishCard.fxml"));
                Pane dishBox = fxmlLoader.load();
                DishCardController dishCardController = fxmlLoader.getController();
                dishCardController.SetData(favoriteRecipe);

                if (column == 1) {
                    column = 0;
                    ++row;
                }

                favoriteGridContainer.add(dishBox, column++, row);
                GridPane.setMargin(dishBox, new Insets(5));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}