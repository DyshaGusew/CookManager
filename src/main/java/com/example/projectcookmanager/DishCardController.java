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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
    private ImageView basketImage;

    @FXML
    private Button basketBtn;

    @FXML
    private ImageView rating;

    private DishCard dishCard;

    private Recipe thisRecipe;

    public void setData(DishCard dish, Recipe recipe) {
        System.out.println("Путь к изображению: " + dish.getImageUrl());

        URL imageURL = getClass().getResource(dish.getImageUrl());

        if (imageURL != null) {
            initializeImageViews(dish.getImageUrl(), dish.getRatingUrl());
            initializeLabels(dish.getName(), dish.getTime(), dish.getCalories());

            boolean isFavorite = isRecipeFavorite(recipe);
            System.out.println("Блюдо " + dish.getName() + "В любимом " + isFavorite);
            boolean isBasket = isRecipeBasket(recipe);
            initializeHeartImage(isFavorite);
            initializeBasketImage(isBasket);

            thisRecipe = recipe;
            dishCard = dish;
        } else {
            System.out.println("URL изображения не найден:" + dish.getImageUrl());
        }
    }

    private void initializeImageViews(String imageUrl, String ratingUrl) {
        Image dishImage = loadImage(imageUrl);
        this.dishImage.setImage(dishImage);
        this.dishImage.setFitWidth(165);
        this.dishImage.setFitHeight(106);

        Image ratingImage = loadImage(ratingUrl);
        this.rating.setImage(ratingImage);
    }

    private void initializeLabels(String name, String time, String calories) {
        dishName.setText(name);
        dishTime.setText(time);
        dishCalories.setText(calories);
    }

    public void initializeHeartImage(boolean isFavorite) {
        heartImage.setImage(loadHeartImage(isFavorite));
    }

    public void initializeBasketImage(boolean isBasket) {
        basketImage.setImage(loadBasketImage(isBasket));
    }

    private Image loadImage(String imageUrl) {
        return new Image(getClass().getResourceAsStream(imageUrl));
    }

    private Image loadHeartImage(boolean isFavorite) {
        String imageUrl = isFavorite ? ImagesUrl.HEART_GREEN.getUrl() : ImagesUrl.HEART_BLACK.getUrl();
        return new Image(getClass().getResourceAsStream(imageUrl));
    }

    private Image loadBasketImage(boolean isBasket) {
        String imageUrl = isBasket ? ImagesUrl.BASKET_GREEN.getUrl() : ImagesUrl.BASKET_GRAY.getUrl();
        return new Image(getClass().getResourceAsStream(imageUrl));
    }

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
    void addLikeRecipe(ActionEvent event) {
        boolean isFavorite = isRecipeFavorite(thisRecipe);

        if (isFavorite) {
            new DBFavoritesRecipes().Delete(thisRecipe.id);
        } else {
            new DBFavoritesRecipes().WriteInFavorite(thisRecipe);
        }

        initializeHeartImage(!isFavorite);
    }

    @FXML
    void addBasketRecipe(ActionEvent event) {
        boolean isBasket = isRecipeBasket(thisRecipe);

        if (isBasket) {
            new DBBasketRecipes().Delete(thisRecipe.id);
        } else {
            new DBBasketRecipes().WriteInBasket(thisRecipe);
        }

        initializeBasketImage(!isBasket);
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