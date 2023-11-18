package com.example.projectcookmanager;

import DishModel.DishCard;
import com.example.projectcookmanager.DataBases.DBBasketRecipes;
import com.example.projectcookmanager.DataBases.DBFavoritesRecipes;
import com.example.projectcookmanager.Entity.Recipe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.example.projectcookmanager.FoodViewController;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DishCardController {
    @FXML
    private Pane dishBox;

    @FXML
    private Label dishName;

    @FXML
    private Label dishTime;

    @FXML
    private Label dishCalories;

    @FXML
    private ImageView dishImage;

    @FXML
    private Button nextBtn;

    @FXML
    private Button likeBtn;

    @FXML
    private ImageView heartImage;


    @FXML
    private Button basketBtn;

    @FXML
    private ImageView rating;

    private DishCard dishCard;

    private Recipe thisRecipe;

    private FullReceiptCardController fullReceiptCardController;

    public void SetData(DishCard dish, Recipe recipe) {
        System.out.println("Путь к изображению: " + dish.getImageUrl());

        // Получение URL изображения
        URL imageURL = getClass().getResource(dish.getImageUrl());

        if (imageURL != null) {
            String imageUrl = imageURL.toExternalForm();
            System.out.println("URL изображения: " + imageUrl);

            // Загрузка изображения по URL
            Image image = new Image(imageUrl);
            dishImage.setImage(image);
            dishImage.setFitWidth(165);
            dishImage.setFitHeight(106);
            dishName.setText(dish.getName());
            dishTime.setText(dish.getTime());
            dishCalories.setText(dish.getCalories());

            // Загрузка рейтингового изображения из ресурсов
            Image ratingImage = new Image(getClass().getResourceAsStream(dish.getRatingUrl()));
            rating.setImage(ratingImage);

            thisRecipe = recipe;
            dishCard = dish;
        } else {
            System.out.println("URL изображения не найден:" + dish.getImageUrl());
        }
    }

    @FXML
    void ShowFullReceipt(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FullReceiptCard.fxml"));
            Parent root = loader.load();

            FullReceiptCardController controller = loader.getController();

            controller.setData(dishCard);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void AddLikeRecipe(ActionEvent event) {
//        boolean isFavorite = isRecipeFavorite(thisRecipe);
//        if (isFavorite) {
//            new DBFavoritesRecipes().Delete(thisRecipe.id);
//            heartImage.setImage(new Image("/img/icons8-black-50.png"));
//        } else {
//            new DBFavoritesRecipes().WriteInFavorite(thisRecipe);
//            heartImage.setImage(new Image("/img/icons8-heart-green.png"));
//        }
    }

    private boolean isRecipeFavorite(Recipe recipe) {
        List<Recipe> favoriteRecipes = new DBFavoritesRecipes().ReadAllOnFavorite();
        return favoriteRecipes.contains(recipe);
    }

    @FXML
    void AddBasketRecipe(ActionEvent event) {
        new DBBasketRecipes().WriteInBasket(thisRecipe);
    }

}