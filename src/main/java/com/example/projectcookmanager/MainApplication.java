package com.example.projectcookmanager;

import com.example.projectcookmanager.DataBases.DBAllRecipes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

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

        //List<Recipe> recipes = new DBAllRecipes().SelectAll();
        //new DBAllRecipes().insert("Гусь", "Нам нямка");
        //Recipe rec = new Recipe("Гусь", "sdf", 20, "2", 250, 10, 5, 15, 2.5f, new String[]{"Сало", "лук", "гусь"}, new String[]{"200гр", "2шт", "1кг"}, new String[]{"2_1", "2_2", "2_3"}, new String[]{"Размораживаем гуся, готовим элементы", "Режем все", "Фигачим в духовку"});

        //Recipe rec2 = new DBAllRecipes().Select("НЕ Гусь");
        //rec2.name = "НdfвамвамссчмчсмsdfЕ Гусь";
       // new DBAllRecipes().Insert(rec2);

        launch();

    }
}