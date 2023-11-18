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
import javafx.geometry.Insets;
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
import javafx.stage.Stage;

import java.util.List;

public class FullReceiptCardController {

    @FXML
    private ImageView choosenImage;

    @FXML
    private Label descriptionArea;

    @FXML
    private Label dishCalloriesLabel;

    @FXML
    private Label dishCategoryLabel;

    @FXML
    private Label dishName;

    @FXML
    private Label dishTime;

    @FXML
    private Label fitLabel;

    @FXML
    private Label carLabel;

    @FXML
    private Label proteinLabel;

    @FXML
    private ImageView time;

    @FXML
    private ListView<String> listViewOfIngredients;

    @FXML
    private ListView<String> listViewOfMass;

    @FXML
    private ImageView rating;

    @FXML
    private ScrollPane stepsScroll;

    @FXML
    private ScrollPane stepsScrollInfo;

    @FXML
    private Button deleteRecipe;

    private DBAllRecipes dbAllRecipes = new DBAllRecipes();


    public void setData(DishCard dish) {
        Recipe recipe = dbAllRecipes.Read(dish.getName());
        dishName.setText(dish.getName());
        choosenImage.setImage(new Image((getClass().getResourceAsStream(dish.getImageUrl()))));
        choosenImage.setFitWidth(205);
        choosenImage.setFitHeight(206);
        rating.setImage(new Image((getClass().getResourceAsStream(dish.getRatingUrl()))));
        dishTime.setText(dish.getTime());

        descriptionArea.setText(recipe.getMainInfo());
        dishCalloriesLabel.setText(String.valueOf(recipe.getCalories()) + " Ккал");
        dishCategoryLabel.setText(recipe.getCategory());

        fitLabel.setText(Integer.toString((int)recipe.getFat()));
        proteinLabel.setText(Integer.toString((int)recipe.getProtein()));
        carLabel.setText(Integer.toString((int)recipe.getCarbohydrate()));

        VBox stepsVbox = new VBox();


        for (int i = 0; i < recipe.getTextStages().size(); i++) {

            String stepText = recipe.getTextStages().get(i);
            String imageUrl = recipe.getImagesStageLinks().get(i);

            //HBox stepBox = new HBox();

            Label stepTextNode = new Label(stepText);
            stepTextNode.setFont(Font.font("Arial", FontWeight.NORMAL, FontPosture.REGULAR, 20));
            stepTextNode.setMaxWidth(270);
            stepTextNode.setWrapText(true);
            stepTextNode.setPadding(new Insets(2, 0, 0, 5));

            ImageView stepImageNode = new ImageView(new Image(getClass().getResourceAsStream("/img/StageRecipe/" + imageUrl)));
            stepImageNode.setFitWidth(250);
            stepImageNode.setFitHeight(250);

            stepsVbox.getChildren().add(stepTextNode);

            ImageView stepImageNode1 = new ImageView();
            stepImageNode1.setFitWidth(5);
            stepImageNode1.setFitHeight(5);
            stepsVbox.getChildren().add(stepImageNode1);

            stepsVbox.getChildren().add(stepImageNode);

            stepImageNode = new ImageView();
            stepImageNode.setFitWidth(20);
            stepImageNode.setFitHeight(20);

            stepsVbox.getChildren().add(stepImageNode);
           // stepsVbox.setSpacing(50);
        }

        stepsScroll.setContent(null);

        stepsScroll.setContent(stepsVbox);

        stepsScrollInfo.setContent(null);

        stepsScrollInfo.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        stepsScrollInfo.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        stepsScrollInfo.setContent(descriptionArea);

        List<Product> allProductsRecipe = new DBRecConnectProd().ReadAllOfRecipe(dbAllRecipes.Read(dish.getName()).id);

        ObservableList<String> ingredientNames = FXCollections.observableArrayList();
        ObservableList<String> ingredientMass = FXCollections.observableArrayList();

        for (Product product : allProductsRecipe) {
            ingredientNames.add(product.name);
            ingredientMass.add(Float.toString(product.getMass()) + " гр");
        }
        listViewOfIngredients.setItems(ingredientNames);
        listViewOfMass.setItems(ingredientMass);
    }

    @FXML
    void DeleteDish(ActionEvent event){
        new DBAllRecipes().Delete(dishName.getText());
        FoodViewController.thisRecipes = new DBAllRecipes().ReadAll();
        FoodViewController.recentlyAdded = FoodViewController.CreateDishCardList(FoodViewController.thisRecipes);

        Stage stage = (Stage) deleteRecipe.getScene().getWindow();
        stage.close();

        //new FoodViewController().updateScrollPane(FoodViewController.recentlyAdded);

    }
}
