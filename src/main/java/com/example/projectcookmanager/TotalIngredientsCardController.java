package com.example.projectcookmanager;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

public class TotalIngredientsCardController {
    @FXML
    private ListView<String> listOfCallories;

    @FXML
    private ListView<String> listOfIngrediens;

    public void SetData(List<String> ingredients, List<String> calories) {
        listOfIngrediens.getItems().setAll(ingredients);
        listOfCallories.getItems().setAll(calories);
    }
}
