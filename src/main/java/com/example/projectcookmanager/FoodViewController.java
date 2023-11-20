package com.example.projectcookmanager;

import DishModel.DishCard;
import com.example.projectcookmanager.DataBases.DBAllProducts;
import com.example.projectcookmanager.DataBases.DBAllRecipes;
import com.example.projectcookmanager.Entity.Product;
import com.example.projectcookmanager.Entity.ProductPattern;
import com.example.projectcookmanager.Entity.Recipe;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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

    private ComboBox<String> ShortBut;

    @FXML
    private MenuButton searchByIngredient;

    @FXML
    private Button CreateNewReceiptBtn;

    @FXML
    private Button ReturnArrayBut;

    @FXML
    private Button favoriteStorage;

    private List<String> selectedIngredients = new ArrayList<>();

    public static List<DishCard> recentlyAdded = new ArrayList<DishCard>();

    //Текущий список рецептов
    public static List<Recipe> thisRecipes = new ArrayList<Recipe>();
    private String thisCategory;

    //Все, что появится в начале
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        InitializeCards();

        handleIngredientsSearchMenu();
    }

    public void InitializeCards()
    {
        //Заполняю комбо бокс
        ObservableList<String> categories = FXCollections.observableArrayList("Новизна", "Время приготовления", "Рейтинг", "Каллорийность");
        ShortBut.setItems(categories);

        //Делаем в перемешанном порядке
        thisRecipes = new DBAllRecipes().ReadAll();
        thisCategory = "all";
        //Collections.reverse(thisRecipes); взащпоовапдаощполапоОООВООВОВОВРОВВРОВРО

        recentlyAdded = new ArrayList<>(CreateDishCardList(thisRecipes));
        int column = 0;
        int row = 1;
        int i = 0;
        try {
            for (DishCard dish : recentlyAdded) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("DishCard.fxml"));
                Pane dishBox = fxmlLoader.load();
                DishCardController dishCardController = fxmlLoader.getController();
                dishCardController.SetData(dish, thisRecipes.get(i));

                if (column == 1) {
                    column = 0;
                    ++row;
                }

                dishContainer.add(dishBox, column++, row);
                GridPane.setMargin(dishBox, new Insets(5));
                i++;
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Обновление показанных рецептов
    public void updateScrollPane(List<DishCard> filteredDishes) {
        dishContainer.getChildren().clear();

        int column = 0;
        int row = 1;

        int i = 0;
        for (DishCard dish : filteredDishes) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("DishCard.fxml"));
                Pane dishBox = fxmlLoader.load();
                DishCardController dishCardController = fxmlLoader.getController();
                dishCardController.SetData(dish, thisRecipes.get(i));

                if (column == 1) {
                    column = 0;
                    ++row;
                }

                dishContainer.add(dishBox, column++, row);
                GridPane.setMargin(dishBox, new Insets(5));
                i++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //Создание списка карт в зависимости от поданного на них списка рецептов из бд
    public static List<DishCard> CreateDishCardList(List<Recipe> recipes) {
        thisRecipes = recipes;
        //recentlyAdded = new ArrayList<>(CreateDishCardList(thisRecipes));
        List<DishCard> dishCardList = new ArrayList<>();

        for(Recipe rec : recipes){
            DishCard dishCard = new DishCard();
            dishCard.setName(rec.name);
            dishCard.setTime(rec.getTimeCooking());
            dishCard.setCalories(rec.getCalories());
            dishCard.setImageUrl(rec.getMainImageLink());
            dishCard.setRatingUrl(rec.getRating());

            dishCardList.add(dishCard);
        }
        return  dishCardList;
    }

    @FXML
    void ClickSearchOfName(){
        ShortBut.setValue("Новизна");
        ReturnArrayBut.setText("▼");
    }

    @FXML
    void SearchOfName(){
        List<Recipe> newList;
        if(!SearchZoneByName.getText().equals("")){
            newList = new DBAllRecipes().ReadOfName(SearchZoneByName.getText());
        }
        else{
            newList = new DBAllRecipes().ReadAll();
        }
        thisRecipes = new ArrayList<Recipe>();

        if(!thisCategory.equals("all")){
            for (Recipe rec : newList){
                if(rec.getCategory().equals(thisCategory)){
                    thisRecipes.add(rec);
                }
            }
            Collections.reverse(thisRecipes);
        }
        else {
            thisRecipes = newList;

        }

        recentlyAdded = new ArrayList<>(CreateDishCardList(thisRecipes));
        updateScrollPane(recentlyAdded);
    }

    @FXML
    void SortOfParams(ActionEvent event){
        List<Recipe> newList;

        switch (ShortBut.getValue()){
//            case "Новизна":
//                newList = new DBAllRecipes().ReadOfSort("id");
//                break;
            case "Время приготовления":
                newList = new DBAllRecipes().ReadOfSort("timeCooking");
                break;
            case "Рейтинг":
                newList = new DBAllRecipes().ReadOfSort("rating");
                break;
            case "Каллорийность":
                newList = new DBAllRecipes().ReadOfSort("calories");
                break;
            default:
                newList = new DBAllRecipes().ReadOfSort("id");
        }

        thisRecipes = new ArrayList<Recipe>();

        //Если категория не содержит всех категорий, то
        if(!thisCategory.equals("all")){
            for(Recipe elem: newList){

                if(elem.getCategory().equals(thisCategory) && elem.name.contains(SearchZoneByName.getText())){
                    thisRecipes.add(elem);
                }
            }
        }
        else {
            thisRecipes = newList;
        }
        Collections.reverse(thisRecipes);

        SearchZoneByName.setText("");
        recentlyAdded = new ArrayList<>(CreateDishCardList(thisRecipes));
        // List<DishCard> filteredDishes = CreateDishCardList(new DBAllRecipes().ReadOfParam("category", "Выпечка"));
        updateScrollPane(recentlyAdded);
    }

    private void handleIngredientSelection(CheckMenuItem selectedMenuItem) {
        System.out.println("Selected ingredient: " + selectedMenuItem.getText());

        selectedMenuItem.setDisable(false);

        if(selectedMenuItem.isSelected()){
            selectedIngredients.add(selectedMenuItem.getText());
        }
        else {
            if (selectedIngredients != null) {
                for (String el : selectedIngredients) {
                    if (el == selectedMenuItem.getText()) {
                        selectedIngredients.remove(el);
                    }
                }
            }
        }
    }

    private void handleIngredientsSearchMenu() {
        searchByIngredient.getItems().clear();

        DBAllProducts dbAllProducts = new DBAllProducts();
        List<ProductPattern> allProducts = dbAllProducts.ReadAll();

        for (ProductPattern productPattern : allProducts) {
            CheckMenuItem checkmenuItem = new CheckMenuItem(productPattern.name);

            checkmenuItem.setOnAction(event -> handleIngredientSearchSelection(checkmenuItem));

            searchByIngredient.getItems().add(checkmenuItem);
        }
    }

    @FXML
    void OpenFavorites(ActionEvent event) {
        OpenNewScene("FavoriteDishes.fxml", "Любимые блюда");
    }

    @FXML
    void OpenBasket(ActionEvent event) {

    }

    private void handleIngredientSearchSelection(CheckMenuItem selectedMenuItem) {
        System.out.println("Selected ingredient: " + selectedMenuItem.getText());

        if(selectedMenuItem.isSelected()){
            selectedIngredients.add(selectedMenuItem.getText());
        }
        else {
            if (selectedIngredients != null) {
                for (String el : selectedIngredients) {
                    if (el == selectedMenuItem.getText()) {
                        selectedIngredients.remove(el);
                        break;
                    }
                }
            }
        }
        selectedMenuItem.setDisable(false);
        thisRecipes = new DBAllRecipes().ReadOfIngrids(selectedIngredients);
        ClickButCategories();
    }

    @FXML
    void ShowListReturn(ActionEvent event) {
        SearchZoneByName.setText("");
        if(ReturnArrayBut.getText().equals("▲")){
            ReturnArrayBut.setText("▼");
        }
        else{
            ReturnArrayBut.setText("▲");
        }
        Collections.reverse(thisRecipes);

        recentlyAdded = new ArrayList<>(CreateDishCardList(thisRecipes));
        //List<DishCard> filteredDishes = CreateDishCardList(thisRecipes);
        updateScrollPane(recentlyAdded);
    }

    @FXML
    void ShowLastAll(ActionEvent event) {
        thisCategory = "all";
        thisRecipes = new DBAllRecipes().ReadAll();
        ClickButCategories();
    }

    @FXML
    void ShowBakery(ActionEvent event) {
        thisRecipes = new DBAllRecipes().ReadOfCategory("Выпечка");
        thisCategory = "Выпечка";
        ClickButCategories();
    }

    @FXML
    void ShowDesserts(ActionEvent event) {
        thisRecipes = new DBAllRecipes().ReadOfCategory("Десерты");
        thisCategory = "Десерты";
        ClickButCategories();
    }

    @FXML
    void ShowDrinks(ActionEvent event) {
        thisRecipes = new DBAllRecipes().ReadOfCategory("Напитки");
        thisCategory = "Напитки";
        ClickButCategories();
    }

    @FXML
    void ShowHotDishes(ActionEvent event) {
        thisRecipes = new DBAllRecipes().ReadOfCategory("Вторые блюда");
        thisCategory = "Вторые блюда";
        ClickButCategories();
    }

    @FXML
    void ShowSalades(ActionEvent event) {
        thisRecipes = new DBAllRecipes().ReadOfCategory( "Салаты");
        thisCategory = "Салаты";
        ClickButCategories();
    }

    @FXML
    void ShowSnacks(ActionEvent event) {
        thisRecipes = new DBAllRecipes().ReadOfCategory("Закуски");
        thisCategory = "Закуски";
        ClickButCategories();
    }

    @FXML
    void ShowSoups(ActionEvent event) {
        thisRecipes = new DBAllRecipes().ReadOfCategory("Супы");
        thisCategory = "Супы";
        ClickButCategories();
    }

    private void BaseView(){
        ShortBut.setValue("Новизна");
        ReturnArrayBut.setText("▼");
        SearchZoneByName.setText("");
    }

    public void ClickButCategories(){
        BaseView();

        Collections.reverse(thisRecipes);
        recentlyAdded = new ArrayList<>(CreateDishCardList(thisRecipes));
        updateScrollPane(recentlyAdded);
    }

    @FXML
    void OpenNewReceiptCard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewReceiptCard.fxml"));
            loader.setControllerFactory(c -> new NewReceiptCardController());
            Parent root = loader.load();

            NewReceiptCardController controller = loader.getController();
            controller.setFoodViewController(this);

            Stage stage = new Stage();
            stage.setTitle("Создание рецепта");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setOnHidden(e -> {
                initializeAfterClose();
            });

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeAfterClose() {
        Platform.runLater(() -> {
            InitializeCards();
            handleIngredientsSearchMenu();
            updateDishCards();
        });
    }

    public void updateDishCards() {
        if (thisRecipes != null) {
            thisRecipes = new DBAllRecipes().ReadAll();
            recentlyAdded = new ArrayList<>(CreateDishCardList(thisRecipes));
            updateScrollPane(recentlyAdded);
        }
    }

    public void OpenNewScene(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            NewReceiptCardController controller = loader.getController();
            controller.setFoodViewController(this);

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);


            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}