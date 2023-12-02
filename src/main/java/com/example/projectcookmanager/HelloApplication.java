package com.example.projectcookmanager;

import com.example.projectcookmanager.DataBases.DBAllRecipes;
import com.example.projectcookmanager.Entity.Recipe;
import com.example.projectcookmanager.Parser.Parser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Food-ReceiptNew.fxml"));
        Parent root = loader.load();
        FoodViewController.foodViewController = loader.getController();
        stage.setTitle("Рецепты");
        stage.setScene(new Scene(root, 930, 635));
        stage.show();
        stage.setResizable(false);
    }

    public static void main(String[] args) {
        //https://povar.ru/recipes/pechene_Minutka_bez_margarina-22188.html
        launch();
    }
}