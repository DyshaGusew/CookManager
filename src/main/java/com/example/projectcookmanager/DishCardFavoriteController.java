package com.example.projectcookmanager;

import DishModel.DishCard;
import DishModel.ImagesUrl;
import com.example.projectcookmanager.DataBases.DBBasketRecipes;
import com.example.projectcookmanager.DataBases.DBFavoritesRecipes;
import com.example.projectcookmanager.Entity.Recipe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DishCardFavoriteController extends DishCardController {
    public static FavoriteListCardController favoriteListCardController;

    @FXML
    private Label dishName;

    @FXML
    private Label dishTime;

    @FXML
    private Label dishCalories;

    @FXML
    private ImageView dishImage;

    @FXML
    private ImageView heartImage;

    @FXML
    private ImageView basketImage;

    @FXML
    private ImageView rating;

    private DishCard dishCard;

    private Recipe thisRecipe;

    @Override
    public void setData(DishCard dish, Recipe recipe) {
        System.out.println("Путь к изображению: " + dish.getImageUrl());

        URL imageURL = getClass().getResource(dish.getImageUrl());

        if (imageURL != null) {
            initializeImageView(dish.getImageUrl(), dishImage, 165, 106);
            dishName.setText(dish.getName());
            dishTime.setText(dish.getTime());
            dishCalories.setText(dish.getCalories());

            Image ratingImage = new Image(getClass().getResourceAsStream(dish.getRatingUrl()));
            rating.setImage(ratingImage);

            boolean isFavorite = isRecipeFavorite(recipe);
            boolean isBasket = isRecipeBasket(recipe);
            initializeHeartImage(isFavorite);
            initializeBasketImage(isBasket);

            thisRecipe = recipe;
            dishCard = dish;
        } else {
            System.out.println("URL изображения не найден:" + dish.getImageUrl());
        }
    }

    @FXML
    void addLikeRecipe(ActionEvent event) {
        boolean isFavorite = isRecipeFavorite(thisRecipe);
        updateHeartImage(isFavorite);
    }

    @Override
    @FXML
    void showFullReceipt(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FullReceiptCard.fxml"));
            Parent root = loader.load();

            FullReceiptCardController controller = loader.getController();
            FullReceiptCardController.fullReceiptCardController = controller;
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
    void addBasketRecipe(ActionEvent event) {
        boolean isBasket = isRecipeBasket(thisRecipe);
        updateBasketImage(isBasket);

        FoodViewController.foodViewController.updateScrollPane(FoodViewController.recentlyAdded);
    }

    private void initializeImageView(String imageUrl, ImageView imageView, double fitWidth, double fitHeight) {
        URL imageURL = getClass().getResource(imageUrl);

        if (imageURL != null) {
            InputStream imageStream = getClass().getResourceAsStream(imageUrl);
            Image image = new Image(imageStream);
            imageView.setImage(image);
            imageView.setFitWidth(fitWidth);
            imageView.setFitHeight(fitHeight);
        } else {
            System.out.println("URL изображения не найден:" + imageUrl);
        }
    }

    private void updateHeartImage(boolean isFavorite) {
        if (isFavorite) {
            new DBFavoritesRecipes().Delete(thisRecipe.id);
            heartImage.setImage(new Image(getClass().getResourceAsStream(ImagesUrl.HEART_BLACK.getUrl())));
        }

        favoriteListCardController.initializeFavoriteDishes();
        FoodViewController.foodViewController.updateScrollPane(FoodViewController.recentlyAdded);
    }

    private void updateBasketImage(boolean isBasket) {
        if (isBasket) {
            new DBBasketRecipes().Delete(thisRecipe.id);
            basketImage.setImage(new Image(getClass().getResourceAsStream(ImagesUrl.BASKET_GRAY.getUrl())));
        } else {
            new DBBasketRecipes().WriteInBasket(thisRecipe);
            basketImage.setImage(new Image(getClass().getResourceAsStream(ImagesUrl.BASKET_GREEN.getUrl())));
        }
    }
}