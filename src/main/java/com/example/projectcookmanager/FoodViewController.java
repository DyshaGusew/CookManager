package com.example.projectcookmanager;

import DishModel.DishCard;
import DishModel.ParsedDishes;
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
import java.util.List;
import java.util.ResourceBundle;

public class FoodViewController implements Initializable {


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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        recentlyAdded = new ArrayList<>(recentlyAdded());
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

    //Добавление блюд ручками
    private List<DishCard> recentlyAdded() {
        List<DishCard> dishCardList = new ArrayList<>();
        DishCard dishCard = new DishCard();
        dishCard.setName("Яичница обычная");
        dishCard.setImageUrl("/img/yaichnica_obichnaya-384161.jpg");
        dishCard.setRatingUrl("/img/4stars.png");
        dishCard.setCategory(DishCard.DishCategory.HotDishes);

        dishCardList.add(dishCard);

        dishCard = new DishCard();
        dishCard.setName("Узбекский плов");
        dishCard.setImageUrl("/img/uzbekskii_plov-4860.jpg");
        dishCard.setRatingUrl("/img/4stars.png");
        dishCard.setCategory(DishCard.DishCategory.HotDishes);

        dishCardList.add(dishCard);

        dishCard = new DishCard();
        dishCard.setName("Мясо по-французски");
        dishCard.setImageUrl("/img/quotmyaso_po-francuzskiquot_iz_kurinogo_farsha-44773.jpg");
        dishCard.setRatingUrl("/img/5stars.png");
        dishCard.setCategory(DishCard.DishCategory.Snack);

        dishCardList.add(dishCard);

        dishCard = new DishCard();
        dishCard.setName("Плов с курицей в мультиварке");
        dishCard.setImageUrl("/img/plov_s_kuricei_v_multivarke-53899.jpg");
        dishCard.setRatingUrl("/img/4stars.png");
        dishCard.setCategory(DishCard.DishCategory.Snack);

        dishCardList.add(dishCard);

        dishCard = new DishCard();
        dishCard.setName("Мясо по-французски с картофелем");
        dishCard.setImageUrl("/img/myaso_po-francuzski_s_kartofelem-45684.jpg");
        dishCard.setRatingUrl("/img/4stars.png");
        dishCard.setCategory(DishCard.DishCategory.Drinks);

        dishCardList.add(dishCard);

        return  dishCardList;
    }

    //Добавление блюд парсером
    private List<DishCard> recentlyAdded(List<ParsedDishes> parsedData) {
        List<DishCard> dishCardList = new ArrayList<>();

        for (ParsedDishes parsedItem : parsedData) {
            DishCard dishCard = new DishCard();
            dishCard.setName(parsedItem.getName());
            dishCard.setImageUrl(parsedItem.getImageUrl());
            dishCard.setRatingUrl(parsedItem.getRatingUrl());
            dishCard.setCategory(parsedItem.getCategory());

            dishCardList.add(dishCard);
        }

        return dishCardList;
    }

    public List<DishCard> getDishesByCategory(DishCard.DishCategory category) {
        List<DishCard> filteredDishes = new ArrayList<>();
        for (DishCard dish : recentlyAdded) {
            if (dish.getCategory().equals(category)) {
                filteredDishes.add(dish);
            }
        }
        return filteredDishes;
    }

    @FXML
    void ShowBakery(ActionEvent event) {
        DishCard.DishCategory category = DishCard.DishCategory.Bakery;
        List<DishCard> filteredDishes = getDishesByCategory(category);
        updateScrollPane(filteredDishes);
    }

    @FXML
    void ShowDesserts(ActionEvent event) {
        DishCard.DishCategory category = DishCard.DishCategory.Dessert;
        List<DishCard> filteredDishes = getDishesByCategory(category);
        updateScrollPane(filteredDishes);
    }

    @FXML
    void ShowDrinks(ActionEvent event) {
        DishCard.DishCategory category = DishCard.DishCategory.Drinks;
        List<DishCard> filteredDishes = getDishesByCategory(category);
        updateScrollPane(filteredDishes);
    }

    @FXML
    void ShowHotDishes(ActionEvent event) {
        DishCard.DishCategory category = DishCard.DishCategory.HotDishes;
        List<DishCard> filteredDishes = getDishesByCategory(category);
        updateScrollPane(filteredDishes);
    }

    @FXML
    void ShowSalades(ActionEvent event) {
        DishCard.DishCategory category = DishCard.DishCategory.Salade;
        List<DishCard> filteredDishes = getDishesByCategory(category);
        updateScrollPane(filteredDishes);
    }

    @FXML
    void ShowSnacks(ActionEvent event) {
        DishCard.DishCategory category = DishCard.DishCategory.Snack;
        List<DishCard> filteredDishes = getDishesByCategory(category);
        updateScrollPane(filteredDishes);
    }

    @FXML
    void ShowSoups(ActionEvent event) {
        DishCard.DishCategory category = DishCard.DishCategory.Soup;
        List<DishCard> filteredDishes = getDishesByCategory(category);
        updateScrollPane(filteredDishes);
    }
}
