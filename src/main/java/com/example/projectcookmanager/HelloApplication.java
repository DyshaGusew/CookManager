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
        Parent root = FXMLLoader.load(getClass().getResource("Food-ReceiptNew.fxml"));
        stage.setTitle("Рецепты");
        stage.setScene(new Scene(root, 930, 635));
        stage.show();
        stage.setResizable(false);
    }

    public static void main(String[] args) {
        //Recipe recOfParser1 = Parser.RecOfParser("https://povar.ru/recipes/idealnoe_testo_dlya_pelmenei-20059.html");
        //Recipe recOfParser2 = Parser.RecOfParser("https://povar.ru/recipes/kurinye_kolbaski_s_syrom_dlya_grilya-70476.html");
        launch();
    }
}