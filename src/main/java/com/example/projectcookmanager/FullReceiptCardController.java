package com.example.projectcookmanager;

import com.example.projectcookmanager.Entity.ProductPattern;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

public class FullReceiptCardController {
    @FXML
    private ListView<ProductPattern> IngredientsList;

    @FXML
    private ListView<String> cookingDescriptionList;

    @FXML
    private ImageView recipeImage;

    @FXML
    private Label recipeName;

}
