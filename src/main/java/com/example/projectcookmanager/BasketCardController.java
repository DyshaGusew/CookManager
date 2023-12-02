package com.example.projectcookmanager;

import DishModel.BasketCard;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class BasketCardController {
    @FXML
    private Label dishName;

    @FXML
    private ListView<String> listOfCallories;

    @FXML
    private ListView<String> listOfIngrediens;

    public void setData(BasketCard basketCard) {
        dishName.setText(basketCard.getDishName());
        listOfIngrediens.getItems().setAll(basketCard.getListOfIngredients());
        listOfCallories.getItems().setAll(basketCard.getListOfCalories());
    }
}
