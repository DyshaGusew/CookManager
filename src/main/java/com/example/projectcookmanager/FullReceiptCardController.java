package com.example.projectcookmanager;

import DishModel.DishCard;
import com.example.projectcookmanager.DataBases.DBAllRecipes;
import com.example.projectcookmanager.DataBases.DBRecConnectProd;
import com.example.projectcookmanager.Entity.Product;
import com.example.projectcookmanager.Entity.Recipe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class FullReceiptCardController {
    @FXML
    private ImageView choosenImage, rating;

    @FXML
    private Label descriptionArea, dishCalloriesLabel, dishCategoryLabel, dishName, dishTime, fitLabel, carLabel, proteinLabel;

    @FXML
    private ListView<String> listViewOfIngredients, listViewOfMass;

    @FXML
    private ScrollPane stepsScroll, stepsScrollInfo;

    @FXML
    private Button deleteRecipe, rewriteBtn;

    private DishCard dishCard;

    public static FullReceiptCardController fullReceiptCardController;

    private DBAllRecipes dbAllRecipes = new DBAllRecipes();

    public void setData(DishCard dish) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewReceiptCard.fxml"));
        loader.setControllerFactory(c -> new NewReceiptCardController());
        Parent root = loader.load();

        NewReceiptCardController controller = loader.getController();
        NewReceiptCardController.newReceiptCardController = controller;

        Recipe recipe = dbAllRecipes.Read(dish.getName());

        initializeBasicInfo(dish, recipe);
        initializeSteps(recipe);
        initializeIngredients(recipe);

        dishCard = dish;
    }

    private void initializeBasicInfo(DishCard dish, Recipe recipe) {
        dishName.setText(dish.getName());
        choosenImage.setImage(new Image((getClass().getResourceAsStream(dish.getImageUrl()))));
        choosenImage.setFitWidth(205);
        choosenImage.setFitHeight(206);
        rating.setImage(new Image((getClass().getResourceAsStream(dish.getRatingUrl()))));
        dishTime.setText(dish.getTime());

        descriptionArea.setText(recipe.getMainInfo());
        dishCalloriesLabel.setText(recipe.getCalories() + " Ккал");
        dishCategoryLabel.setText(recipe.getCategory());
        fitLabel.setText(String.valueOf((int) recipe.getFat()));
        proteinLabel.setText(String.valueOf((int) recipe.getProtein()));
        carLabel.setText(String.valueOf((int) recipe.getCarbohydrate()));
    }

    private void initializeSteps(Recipe recipe) {
        VBox stepsVbox = new VBox();

        for (int i = 0; i < recipe.getTextStages().size(); i++) {
            String stepText = recipe.getTextStages().get(i);
            String imageUrl = recipe.getImagesStageLinks().get(i);

            Label stepTextNode = createStepLabel(stepText);
            ImageView stepImageNode = createStepImageView(imageUrl);

            stepsVbox.getChildren().addAll(stepTextNode, new ImageView(), stepImageNode, new ImageView());
        }

        stepsScroll.setContent(stepsVbox);
        stepsScrollInfo.setContent(descriptionArea);
    }

    private Label createStepLabel(String stepText) {
        Label stepTextNode = new Label(stepText);
        stepTextNode.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 20));
        stepTextNode.setMaxWidth(270);
        stepTextNode.setWrapText(true);
        stepTextNode.setPadding(new Insets(2, 0, 0, 5));
        return stepTextNode;
    }

    private ImageView createStepImageView(String imageUrl) {
        URL imageURL = getClass().getResource("/img/StageRecipe/" + imageUrl);

        if (imageURL != null) {
            InputStream imageStream = null;
            try {
                imageStream = imageURL.openStream();
                Image image = new Image(imageStream);
                ImageView stepImageNode = new ImageView(image);
                stepImageNode.setFitWidth(250);
                stepImageNode.setFitHeight(250);
                return stepImageNode;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (imageStream != null) {
                    try {
                        imageStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("Ссылка на изображение этапа пуста для этапа");
        }

        return new ImageView();
    }

    private void initializeIngredients(Recipe recipe) {
        List<Product> allProductsRecipe = new DBRecConnectProd().ReadAllOfRecipe(dbAllRecipes.Read(recipe.name).id);

        ObservableList<String> ingredientNames = FXCollections.observableArrayList();
        ObservableList<String> ingredientMass = FXCollections.observableArrayList();

        for (Product product : allProductsRecipe) {
            ingredientNames.add(product.name);
            ingredientMass.add(product.getMass() + " гр");
        }

        listViewOfIngredients.setItems(ingredientNames);
        listViewOfMass.setItems(ingredientMass);
    }

    @FXML
    void deleteDish(ActionEvent event) {
        new DBAllRecipes().Delete(dishName.getText());
        FoodViewController.thisRecipes = new DBAllRecipes().ReadAll();
        FoodViewController.recentlyAdded = FoodViewController.createDishCardList(FoodViewController.thisRecipes);
        FoodViewController.foodViewController.updateScrollPane(FoodViewController.recentlyAdded);
        ((Stage) deleteRecipe.getScene().getWindow()).close();
    }

    @FXML
    void rewriteReceipt(ActionEvent event) {
        Recipe selectedRecipe = dbAllRecipes.Read(dishName.getText());

        if (selectedRecipe != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("RewriteReceiptCard.fxml"));
                loader.setControllerFactory(c -> new NewReceiptCardController());
                Parent root = loader.load();

                NewReceiptCardController controller = loader.getController();
                NewReceiptCardController.newReceiptCardController = controller;

                NewReceiptCardController.newReceiptCardController.InitData(dishCard);

                Stage stage = new Stage();
                stage.setTitle("Ингредиенты для блюд");
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);

                stage.showAndWait();
                stage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
