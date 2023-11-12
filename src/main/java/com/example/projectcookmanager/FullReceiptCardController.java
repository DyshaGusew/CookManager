package com.example.projectcookmanager;

import DishModel.DishCard;
import com.example.projectcookmanager.DataBases.DBAllProducts;
import com.example.projectcookmanager.DataBases.DBAllRecipes;
import com.example.projectcookmanager.Entity.Product;
import com.example.projectcookmanager.Entity.ProductPattern;
import com.example.projectcookmanager.Entity.Recipe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class FullReceiptCardController {

    @FXML
    private ImageView choosenImage;

    @FXML
    private TextArea decriptionArea;

    @FXML
    private Label dishCalloriesLabel;

    @FXML
    private Label dishCategoryLabel;

    @FXML
    private Label dishName;

    @FXML
    private Label dishTime;

    @FXML
    private ImageView time;

    @FXML
    private ListView<String> listViewOfIngredients;

    @FXML
    private ImageView rating;

    @FXML
    private ScrollPane stepsScroll;

    private DBAllRecipes dbAllRecipes = new DBAllRecipes();


    public void setData(DishCard dish) {
        Recipe recipe = dbAllRecipes.Read(dish.getName());
        dishName.setText(dish.getName());
        choosenImage.setImage(new Image((getClass().getResourceAsStream(dish.getImageUrl()))));
        choosenImage.setFitWidth(200);
        choosenImage.setFitHeight(200);
        rating.setImage(new Image((getClass().getResourceAsStream(dish.getRatingUrl()))));
        dishTime.setText(dish.getTime());

        decriptionArea.setText(recipe.getMainInfo());
        dishCalloriesLabel.setText(String.valueOf(recipe.getCalories()));
        dishCategoryLabel.setText(recipe.getCategory());

        VBox stepsVbox = new VBox();
        stepsVbox.setSpacing(10);

        for (int i = 0; i < recipe.textStages.length; i++) {
            String stepText = recipe.textStages[i];
            String imageUrl = recipe.imagesStageLinks[i];

            HBox stepBox = new HBox();

            Text stepTextNode = new Text(stepText);

            //ImageView stepImageNode = new ImageView(new Image(imageUrl));
//            stepImageNode.setFitWidth(100);
//            stepImageNode.setFitHeight(100);

            stepBox.getChildren().addAll(stepTextNode);

            stepsVbox.getChildren().add(stepBox);
        }

        stepsScroll.setContent(null);

        stepsScroll.setContent(stepsVbox);

        DBAllProducts dbAllProducts = new DBAllProducts();
        List<ProductPattern> allProducts = dbAllProducts.ReadAll();

        ObservableList<String> ingredientNames = FXCollections.observableArrayList();
        for (ProductPattern productPattern : allProducts) {
            ingredientNames.add(productPattern.name);
        }
        listViewOfIngredients.setItems(ingredientNames);
    }
}
