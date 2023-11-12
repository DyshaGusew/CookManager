package com.example.projectcookmanager;

import DishModel.DishCard;
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

public class DishCardController {
    @FXML
    private Pane dishBox;

    @FXML
    private Label dishName;

    @FXML
    private Label dishTime;

    @FXML
    private ImageView dishImage;

    @FXML
    private Button nextBtn;

    @FXML
    private ImageView rating;

    private DishCard dishCard;

    private FullReceiptCardController fullReceiptCardController;

    public void SetData(DishCard dish) {
        Image image = new Image(getClass().getResourceAsStream(dish.getImageUrl()));
        dishImage.setImage(image);
        dishImage.setFitWidth(165);
        dishImage.setFitHeight(106);
        dishName.setText(dish.getName());
        dishTime.setText(dish.getTime());
        Image ratingImage = new Image(getClass().getResourceAsStream(dish.getRatingUrl()));
        rating.setImage(ratingImage);

        dishCard = dish;
    }

    @FXML
    void ShowFullReceipt(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FullReceiptCard.fxml"));
            Parent root = loader.load();

            FullReceiptCardController controller = loader.getController();

            controller.setData(dishCard);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

