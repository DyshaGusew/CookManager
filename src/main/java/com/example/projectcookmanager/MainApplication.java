package com.example.projectcookmanager;

import com.example.projectcookmanager.DataBases.DBAllProducts;
import com.example.projectcookmanager.DataBases.DBAllRecipes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
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
        List<Product> gg = new ArrayList<Product>();
        gg.add(new DBAllProducts().Read(8));
        gg.add(new DBAllProducts().Read(9));
        gg.add(new DBAllProducts().Read(7));

        Recipe rec = new Recipe("ГусьСвинья228", "Гусенок под соусом", "Жарdfgое", 80, "dsf", gg, 10.5f, new String[]{"200гр", "2шт", "1кг"}, new String[]{"2_1", "2_2", "2_3"}, new String[]{"Размораживаем гуся, готовим элементы", "Режем все", "Фигачим в духовку"});

        //Recipe rec2 = new DBAllRecipes().Read("НЕ Гусь");
        //rec2.name = "НdfвамвамссчмчсмsdfЕ Гусь";
        //new DBAllRecipes().Write(rec2);
        //List<Recipe> recipes = new  DBAllRecipes().ReadOfParam("category", "Второе блюдо");
       // List<Recipe> recipes2 = new  DBAllRecipes().ReadOfParam("totalCalories", "177");
       // List<Recipe> recipes3 = new  DBAllRecipes().ReadOfSort("timeCooking");

        //List<Product> pr = new DBAllProducts().ReadAllOfRecipe(2);

        //new DBAllRecipes().Write(rec);
        new  DBAllProducts().UpdateProdRec(new DBAllRecipes().Read("ГусьСвинья228"), gg);
       // new DBAllRecipes().Update("ГусьСвинья8", rec);
       // new DBAllRecipes().Delete("НdfвамвамссчмчсмsdfЕ Гусь");
       // new DBAllRecipes().Write(rec);
        launch();

    }
}