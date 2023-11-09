package com.example.projectcookmanager;

import DishModel.DishCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class DishCardController {
    @FXML
    private Pane dishBox;

    @FXML
    private Label dishName;

    @FXML
    private Label dishTime;

    @FXML
    private ImageView dishImage;

    private FullReceiptCardController fullRecipeController;

    @FXML
    private Button nextBtn;

    @FXML
    private ImageView rating;

    private DishCard dishCard;

    public void SetData(DishCard dish) {
        Image image = new Image(getClass().getResourceAsStream(dish.getImageUrl()));
        dishImage.setImage(image);
        dishImage.setFitWidth(165);
        dishImage.setFitHeight(106);
        dishName.setText(dish.getName());
        dishTime.setText(dish.getTime());
        Image ratingImage = new Image(getClass().getResourceAsStream(dish.getRatingUrl()));
        rating.setImage(ratingImage);
    }

    @FXML
    void ShowFullReceipt(ActionEvent event) {
    }


}
