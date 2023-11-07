package com.example.projectcookmanager;

import DishModel.DishCard;
//import DishModel.Recipe;
import com.example.projectcookmanager.DataBases.DBAllRecipes;
import com.example.projectcookmanager.Entity.Recipe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class FoodViewController implements Initializable {


    @FXML
    private Button allDishBtn;

    @FXML
    private Button bakeryBtn;

    @FXML
    private Button dessertBtn;

    @FXML
    private Button drinksBtn;

    @FXML
    private Button hotDishBtn;

    @FXML
    private Button saladeBtn;

    @FXML
    private Button snackBtn;

    @FXML
    private Button soupBtn;

    @FXML
    private GridPane dishContainer;

    @FXML
    private Label NameOfDish;

    @FXML
    private VBox FoodReceiptVbox;

    @FXML
    private TextField SearchZoneByName;

    @FXML
    private TextField SearchZoneByIngridient;

    @FXML
    private Button CreateNewReceiptBtn;

    private List<DishCard> recentlyAdded;

    //Текущий список рецептов
    private List<Recipe> thisRecipes;

    //Все, что появится в начале
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Делаем в перемешанном порядке
        thisRecipes = new DBAllRecipes().ReadAll();
        Collections.reverse(thisRecipes);

        recentlyAdded = new ArrayList<>(CreateDishCardList(thisRecipes));
        int column = 0;
        int row = 1;
        try {
            for (DishCard dish : recentlyAdded) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("DishCard.fxml"));
                Pane dishBox = fxmlLoader.load();
                DishCardController dishCardController = fxmlLoader.getController();
                dishCardController.SetData(dish);

                if (column == 1) {
                    column = 0;
                    ++row;
                }

                dishContainer.add(dishBox, column++, row);
                GridPane.setMargin(dishBox, new Insets(5));
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Обновление показанных рецептов
    private void updateScrollPane(List<DishCard> filteredDishes) {
        dishContainer.getChildren().clear();

        int column = 0;
        int row = 1;

        for (DishCard dish : filteredDishes) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("DishCard.fxml"));
                Pane dishBox = fxmlLoader.load();
                DishCardController dishCardController = fxmlLoader.getController();
                dishCardController.SetData(dish);

                if (column == 1) {
                    column = 0;
                    ++row;
                }

                dishContainer.add(dishBox, column++, row);
                GridPane.setMargin(dishBox, new Insets(5));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Создание списка карт в зависимости от поданного на них списка рецептов из бд
    private List<DishCard> CreateDishCardList(List<Recipe> recipes) {
        thisRecipes = recipes;
        //recentlyAdded = new ArrayList<>(CreateDishCardList(thisRecipes));
        List<DishCard> dishCardList = new ArrayList<>();

        for(Recipe rec : recipes){
            DishCard dishCard = new DishCard();
            dishCard.setName(rec.name);
            dishCard.setImageUrl(rec.getMainImageLink());
            dishCard.setRatingUrl(rec.getRating());

            dishCardList.add(dishCard);
        }
        return  dishCardList;
    }

    //Добавление блюд парсером
//    private List<DishCard> recentlyAdded(List<ParsedDishes> parsedData) {
//        List<DishCard> dishCardList = new ArrayList<>();
//
//        for (ParsedDishes parsedItem : parsedData) {
//            DishCard dishCard = new DishCard();
//            dishCard.setName(parsedItem.getName());
//            dishCard.setImageUrl(parsedItem.getImageUrl());
//           // dishCard.setRatingUrl(parsedItem.getRatingUrl());
//
//            dishCardList.add(dishCard);
//        }
//
//        return dishCardList;
//    }

    @FXML
    void ShowLastAll(ActionEvent event) {
        thisRecipes = new DBAllRecipes().ReadAll();
        Collections.reverse(thisRecipes);

        recentlyAdded = new ArrayList<>(CreateDishCardList(thisRecipes));
        //List<DishCard> filteredDishes = CreateDishCardList(thisRecipes);
        updateScrollPane(recentlyAdded);
    }

    @FXML
    void ShowBakery(ActionEvent event) {
        thisRecipes = new DBAllRecipes().ReadOfCategory("Выпечка");
        recentlyAdded = new ArrayList<>(CreateDishCardList(thisRecipes));
       // List<DishCard> filteredDishes = CreateDishCardList(new DBAllRecipes().ReadOfParam("category", "Выпечка"));
        updateScrollPane(recentlyAdded);
    }

    @FXML
    void ShowDesserts(ActionEvent event) {
        thisRecipes = new DBAllRecipes().ReadOfCategory("Десерты");
        recentlyAdded = new ArrayList<>(CreateDishCardList(thisRecipes));
       // List<DishCard> filteredDishes = CreateDishCardList(new DBAllRecipes().ReadOfParam("category", "Десерты"));
        updateScrollPane(recentlyAdded);
    }

    @FXML
    void ShowDrinks(ActionEvent event) {
        thisRecipes = new DBAllRecipes().ReadOfCategory("Напитки");
        recentlyAdded = new ArrayList<>(CreateDishCardList(thisRecipes));
       // List<DishCard> filteredDishes = CreateDishCardList(new DBAllRecipes().ReadOfParam("category", "Напитки"));
        updateScrollPane(recentlyAdded);
    }

    @FXML
    void ShowHotDishes(ActionEvent event) {
        thisRecipes = new DBAllRecipes().ReadOfCategory("Вторые блюда");
        recentlyAdded = new ArrayList<>(CreateDishCardList(thisRecipes));
       // List<DishCard> filteredDishes = CreateDishCardList(new DBAllRecipes().ReadOfParam("category", "Вторые блюда"));
        updateScrollPane(recentlyAdded);
    }

    @FXML
    void ShowSalades(ActionEvent event) {
        thisRecipes = new DBAllRecipes().ReadOfCategory( "Салаты");
        recentlyAdded = new ArrayList<>(CreateDishCardList(thisRecipes));

       // List<DishCard> filteredDishes = CreateDishCardList(thisRecipes);

        updateScrollPane(recentlyAdded);
    }

    @FXML
    void ShowSnacks(ActionEvent event) {
        thisRecipes = new DBAllRecipes().ReadOfCategory("Закуски");
        recentlyAdded = new ArrayList<>(CreateDishCardList(thisRecipes));

       // List<DishCard> filteredDishes = CreateDishCardList(thisRecipes);
        updateScrollPane(recentlyAdded);
    }

    @FXML
    void ShowSoups(ActionEvent event) {
        thisRecipes = new DBAllRecipes().ReadOfCategory("Супы");
        recentlyAdded = new ArrayList<>(CreateDishCardList(thisRecipes));

       // List<DishCard> filteredDishes = CreateDishCardList(thisRecipes);
        updateScrollPane(recentlyAdded);
    }
}
