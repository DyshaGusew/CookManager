package com.example.projectcookmanager;

import com.example.projectcookmanager.DataBases.DBAllRecipes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import static com.example.projectcookmanager.DataBases.DBAllRecipes.*;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("start-list.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("List of recipes");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        List<Recipe> recipes = new DBAllRecipes().SelectAll();
        //new DBAllRecipes().insert("Гусь", "Нам нямка");

        launch();

    }
}