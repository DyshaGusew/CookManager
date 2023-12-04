package com.example.projectcookmanager;

import DishModel.DishCard;
import com.example.projectcookmanager.DataBases.DBAllProducts;
import com.example.projectcookmanager.DataBases.DBAllRecipes;
import com.example.projectcookmanager.Entity.ProductPattern;
import com.example.projectcookmanager.Entity.Recipe;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class FoodViewController implements Initializable {
    public static FoodViewController foodViewController;

    @FXML
    private GridPane dishContainer;

    @FXML
    private TextField SearchZoneByName;

    @FXML
    private ComboBox<String> ShortBut;

    @FXML
    private MenuButton searchByIngredient;

    @FXML
    private Button ReturnArrayBut;

    @FXML
    private ComboBox<String> filterAspectBut;

    @FXML
    private ComboBox<String> filterOperationBut;

    @FXML
    private TextField filterValue;

    private List<String> selectedIngredients = new ArrayList<>();

    public static List<DishCard> recentlyAdded = new ArrayList<>();

    public static List<Recipe> thisRecipes = new ArrayList<>();

    private String thisCategory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeComponents();
        handleIngredientsSearchMenu();
    }

    private void initializeComponents() {
        initializeComboBoxes();
        loadRecipes();
        baseView();
        updateScrollPane(recentlyAdded);
    }

    private void initializeComboBoxes() {
        ObservableList<String> categories = FXCollections.observableArrayList("Новизна", "Время приготовления", "Рейтинг", "Каллорийность");
        ShortBut.setItems(categories);
        ShortBut.setValue("Новизна");

        ObservableList<String> aspects = FXCollections.observableArrayList("Ккал", "Время", "Рейтинг");
        filterAspectBut.setItems(aspects);
        filterAspectBut.setValue("Ккал");

        ObservableList<String> operations = FXCollections.observableArrayList("=", "<", ">");
        filterOperationBut.setItems(operations);
        filterOperationBut.setValue("=");
    }

    private void loadRecipes() {
        thisRecipes = new DBAllRecipes().ReadAll();
        thisCategory = "all";
        recentlyAdded = createDishCardList(thisRecipes);
    }

    private void baseView() {
        ShortBut.setValue("Новизна");
        ReturnArrayBut.setText("▼");
        SearchZoneByName.setText("");
    }

    public void updateScrollPane(List<DishCard> filteredDishes) {
        dishContainer.getChildren().clear();

        int column = 0;
        int row = 1;

        int i = 0;
        for (DishCard dish : filteredDishes) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DishCard.fxml"));
                Pane dishBox = fxmlLoader.load();
                DishCardController dishCardController = fxmlLoader.getController();
                dishCardController.setData(dish, thisRecipes.get(i));

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

    public static List<DishCard> createDishCardList(List<Recipe> recipes) {
        List<DishCard> dishCardList = new ArrayList<>();

        for (Recipe rec : recipes) {
            DishCard dishCard = new DishCard();
            dishCard.setName(rec.name);
            dishCard.setTime(rec.getTimeCooking());
            dishCard.setCalories(rec.getCalories());
            dishCard.setImageUrl(rec.getMainImageLink());
            dishCard.setRatingUrl(rec.getRating());

            dishCardList.add(dishCard);
        }
        return dishCardList;
    }

    @FXML
    void clickSearchOfName(){
        ShortBut.setValue("Новизна");
        ReturnArrayBut.setText("▼");
    }

    @FXML
    void searchOfName(){
        List<Recipe> newList;
        if(!SearchZoneByName.getText().equals("")){
            newList = new DBAllRecipes().ReadOfName(SearchZoneByName.getText());
        }
        else{
            newList = new DBAllRecipes().ReadAll();
        }
        thisRecipes = new ArrayList<>();

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

        recentlyAdded = new ArrayList<>(createDishCardList(thisRecipes));
        updateScrollPane(recentlyAdded);
    }

    @FXML
    void sortOfParams(ActionEvent event){
        List<Recipe> newList;

        switch (ShortBut.getValue()){
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

        thisRecipes = new ArrayList<>();

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
        recentlyAdded = new ArrayList<>(createDishCardList(thisRecipes));
        updateScrollPane(recentlyAdded);
    }


    @FXML
    void filterOfParams(ActionEvent event){
        List<Recipe> newList = new ArrayList<>();
        boolean g1 = filterAspectBut.getValue() == null;
        boolean g2 = filterOperationBut.getValue() == null;
        boolean g3 = filterValue.getText().equals("");
        if(g1 || g2 || g3){
            return;
        }

        switch (filterAspectBut.getValue()){
            case "Ккал":
                newList = new DBAllRecipes().FilterOfParam("calories", filterValue.getText(), filterOperationBut.getValue());
                break;
            case "Рейтинг":
                newList = new DBAllRecipes().FilterOfParam("rating", filterValue.getText(), filterOperationBut.getValue());
                break;
            case "Время":
                newList = new DBAllRecipes().FilterOfParam("timeCooking", filterValue.getText(), filterOperationBut.getValue());
                break;
            default:
                newList = new DBAllRecipes().ReadOfSort("id");
        }

        thisRecipes = new ArrayList<>();

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
        recentlyAdded = new ArrayList<>(createDishCardList(thisRecipes));
        updateScrollPane(recentlyAdded);
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
    void openFavorites(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FavoriteListCard.fxml"));
            Parent root = loader.load();

            FavoriteListCardController controller = loader.getController();
            DishCardFavoriteController.favoriteListCardController = controller;

            Stage stage = new Stage();
            stage.setTitle("Любимые блюда");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setOnCloseRequest(e -> updateDishCards());

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void openBasket(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BasketIngredientsCard.fxml"));
            Parent root = loader.load();

            BasketIngredientsCardController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Ингредиенты для блюд");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        clickButCategories();
    }

    @FXML
    void showListReturn(ActionEvent event) {
        SearchZoneByName.setText("");
        if(ReturnArrayBut.getText().equals("▲")){
            ReturnArrayBut.setText("▼");
        }
        else{
            ReturnArrayBut.setText("▲");
        }
        Collections.reverse(thisRecipes);

        recentlyAdded = new ArrayList<>(createDishCardList(thisRecipes));
        updateScrollPane(recentlyAdded);
    }

    @FXML
    void showLastAll(ActionEvent event) {
        thisCategory = "all";
        thisRecipes = new DBAllRecipes().ReadAll();
        clickButCategories();
    }

    @FXML
    void showBakery(ActionEvent event) {
        thisRecipes = new DBAllRecipes().ReadOfCategory("Выпечка");
        thisCategory = "Выпечка";
        clickButCategories();
    }

    @FXML
    void showDesserts(ActionEvent event) {
        thisRecipes = new DBAllRecipes().ReadOfCategory("Десерты");
        thisCategory = "Десерты";
        clickButCategories();
    }

    @FXML
    void showDrinks(ActionEvent event) {
        thisRecipes = new DBAllRecipes().ReadOfCategory("Напитки");
        thisCategory = "Напитки";
        clickButCategories();
    }

    @FXML
    void showHotDishes(ActionEvent event) {
        thisRecipes = new DBAllRecipes().ReadOfCategory("Горячие блюда");
        thisCategory = "Горячие блюда";
        clickButCategories();
    }

    @FXML
    void showSalades(ActionEvent event) {
        thisRecipes = new DBAllRecipes().ReadOfCategory( "Салаты");
        thisCategory = "Салаты";
        clickButCategories();
    }

    @FXML
    void showSnacks(ActionEvent event) {
        thisRecipes = new DBAllRecipes().ReadOfCategory("Закуски");
        thisCategory = "Закуски";
        clickButCategories();
    }

    @FXML
    void showSoups(ActionEvent event) {
        thisRecipes = new DBAllRecipes().ReadOfCategory("Супы");
        thisCategory = "Супы";
        clickButCategories();
    }

    public void clickButCategories(){
        baseView();

        Collections.reverse(thisRecipes);
        recentlyAdded = new ArrayList<>(createDishCardList(thisRecipes));
        updateScrollPane(recentlyAdded);
    }

    @FXML
    void openNewReceiptCard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewReceiptCard.fxml"));
            loader.setControllerFactory(c -> new NewReceiptCardController());
            Parent root = loader.load();

            NewReceiptCardController controller = loader.getController();
            NewReceiptCardController.newReceiptCardController = controller;
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
            //initializeComponents();
            handleIngredientsSearchMenu();
            updateDishCards();
        });
    }

    public void updateDishCards() {
        if (thisRecipes != null) {
            thisRecipes = new DBAllRecipes().ReadAll();
            recentlyAdded = new ArrayList<>(createDishCardList(thisRecipes));
            updateScrollPane(recentlyAdded);
        }
    }
}