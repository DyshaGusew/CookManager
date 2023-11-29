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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class DishCardController {
    public static FoodViewController foodViewController;

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
    private ImageView basketImage;

    @FXML
    private Button basketBtn;

    @FXML
    private ImageView rating;

    private DishCard dishCard;

    private Recipe thisRecipe;

    public void SetData(DishCard dish, Recipe recipe) {
        System.out.println("Путь к изображению: " + dish.getImageUrl());

        URL imageURL = getClass().getResource(dish.getImageUrl());

        if (imageURL != null) {
            InputStream imageStream = getClass().getResourceAsStream(dish.getImageUrl());

            Image image = new Image(imageStream);
            dishImage.setImage(image);
            dishImage.setFitWidth(165);
            dishImage.setFitHeight(106);
            dishName.setText(dish.getName());
            dishTime.setText(dish.getTime());
            dishCalories.setText(dish.getCalories());

            Image ratingImage = new Image(getClass().getResourceAsStream(dish.getRatingUrl()));
            rating.setImage(ratingImage);

            boolean isFavorite = isRecipeFavorite(recipe);
            boolean isBasket = isRecipeBasket(recipe);
            if (isFavorite) {
                heartImage.setImage(new Image(getClass().getResourceAsStream("/img/icons8-heart-green.png")));
            } else {
                heartImage.setImage(new Image(getClass().getResourceAsStream("/img/icons8-heart-black-50.png")));
            }

            if (isBasket) {
                basketImage.setImage(new Image(getClass().getResourceAsStream("/img/icons8-add-to-basket-shop-green.png")));
            } else {
                basketImage.setImage(new Image(getClass().getResourceAsStream("/img/icons8-add-to-basket-shop-96.png")));
            }

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
            FullReceiptCardController.fullReceiptCardController = controller;

            controller.SetData(dishCard);

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
        boolean isFavorite = isRecipeFavorite(thisRecipe);
        if (isFavorite) {
            new DBFavoritesRecipes().Delete(thisRecipe.id);
            heartImage.setImage(new Image(getClass().getResourceAsStream("/img/icons8-heart-black-50.png")));

        } else {
            new DBFavoritesRecipes().WriteInFavorite(thisRecipe);
            heartImage.setImage(new Image(getClass().getResourceAsStream("/img/icons8-heart-green.png")));
        }
    }

    protected boolean isRecipeFavorite(Recipe recipe) {
        List<Recipe> favoriteRecipes = new DBFavoritesRecipes().ReadAllOnFavorite();
        for(Recipe favRec : favoriteRecipes){
            if(favRec.id == recipe.id){
                return true;
            }
        }
        return false;
    }

    @FXML
    void AddBasketRecipe(ActionEvent event) {
        boolean isBasket = isRecipeBasket(thisRecipe);
        if (isBasket) {
            new DBBasketRecipes().Delete(thisRecipe.id);
            basketImage.setImage(new Image(getClass().getResourceAsStream("/img/icons8-add-to-basket-shop-96.png")));

        } else {
            new DBBasketRecipes().WriteInBasket(thisRecipe);
            basketImage.setImage(new Image(getClass().getResourceAsStream("/img/icons8-add-to-basket-shop-green.png")));
        }
    }

    protected boolean isRecipeBasket(Recipe recipe) {
        List<Recipe> basketRecipes = new DBBasketRecipes().ReadAllOnBasket();
        for(Recipe basRec : basketRecipes){
            if(basRec.id == recipe.id){
                return true;
            }
        }
        return false;
    }
}