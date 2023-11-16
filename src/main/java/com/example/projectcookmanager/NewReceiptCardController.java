package com.example.projectcookmanager;

import DishModel.DishCard;
import DishModel.StepData;
import com.example.projectcookmanager.DataBases.DBAllProducts;
import com.example.projectcookmanager.DataBases.DBAllRecipes;
import com.example.projectcookmanager.Entity.Product;
import com.example.projectcookmanager.Entity.ProductPattern;
import com.example.projectcookmanager.Entity.Recipe;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NewReceiptCardController {
    private String dishName;
    private String selectedCategory;
    private List<String> selectedIngredients = new ArrayList<>();
    private String description;
    private List<StepData> stepsData = new ArrayList<>();
    private String selectedTime;
    private String selectedRating;
    private String imagePath;

    @FXML
    private TextField categoryCondition;

    @FXML
    private Button chooseImageBtn;

    @FXML
    private Button createDishBtn;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private MenuButton dishCategoriesMenu;

    @FXML
    private MenuButton dishIngredientsMenu;

    @FXML
    private TextField dishNameField;

    @FXML
    private ImageView choosenImage;

    @FXML
    private Button showPreviewBtn;

    @FXML
    private MenuButton ratingMenuBtn;

    @FXML
    private MenuButton timeOfCookingMenuBtn;

    @FXML
    private Spinner<Integer> hoursSpinner;

    @FXML
    private Spinner<Integer> minutesSpinner;

    @FXML
    private TextArea stepDescriptionTextArea;

    @FXML
    private TextField selectedTimeTextField;

    @FXML
    private VBox stepsVBox;

    @FXML
    private Label ratingLabel;

    @FXML
    private Button addStepButton;

    @FXML
    private Button removeStepButton;

    @FXML
    private ImageView stepImageView;

    @FXML
    private Button chooseImageForStepButton;

    private List<Node> stepNodes = new ArrayList<>();
    private File selectedFile;

    @FXML
    void initialize() {
        SaveCategoryCondition();

        ChooseTimeOfCooking();

        SaveRatingCondition();

        handleIngredientsMenu();

        addStepButton.setOnAction(event -> addStep());
        removeStepButton.setOnAction(event -> removeStep());
    }

    private void ChooseTimeOfCooking() {
        timeOfCookingMenuBtn.getItems().forEach(menuItem -> {
            if (menuItem instanceof CheckMenuItem) {
                CheckMenuItem checkMenuItem = (CheckMenuItem) menuItem;
                checkMenuItem.setOnAction(e -> {
                    if (checkMenuItem.getText().equals("В часах")) {
                        hoursSpinner.setVisible(true);
                        minutesSpinner.setVisible(false);
                    } else if (checkMenuItem.getText().equals("В минутах")) {
                        hoursSpinner.setVisible(false);
                        minutesSpinner.setVisible(true);
                    }
                });
            }
        });

        hoursSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 48, 0));
        minutesSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 60, 0));
    }

    private void SaveCategoryCondition() {
        dishCategoriesMenu.getItems().forEach(menuItem -> {
            if (menuItem instanceof MenuItem) {
                MenuItem categoryMenuItem = menuItem;
                categoryMenuItem.setOnAction(e -> {
                    String selectedCategory = categoryMenuItem.getText();
                    categoryCondition.setText(selectedCategory);
                });
            }
        });
    }

    private void handleIngredientsMenu() {
        dishIngredientsMenu.getItems().clear();

        DBAllProducts dbAllProducts = new DBAllProducts();
        List<ProductPattern> allProducts = dbAllProducts.ReadAll();

        for (ProductPattern productPattern : allProducts) {
            CheckMenuItem checkmenuItem = new CheckMenuItem(productPattern.name);

            checkmenuItem.setOnAction(event -> handleIngredientSelection(checkmenuItem));

            dishIngredientsMenu.getItems().add(checkmenuItem);
        }
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

    @FXML
    void dishIngredientsMenuClicked(ActionEvent event) {
        handleIngredientsMenu();
    }

    private void SaveRatingCondition() {
        ratingMenuBtn.getItems().forEach(menuItem -> {
            if (menuItem instanceof MenuItem) {
                menuItem.setOnAction(e -> updateRatingLabel(menuItem.getText()));
            }
        });
    }

    private void addStep() {
        VBox stepNode = new VBox();

        ImageView stepImageView = new ImageView();
        Button chooseImageForStepButton = new Button("Выбрать изображение");
        chooseImageForStepButton.setOnAction(event -> chooseImageForStep(stepImageView));

        TextArea stepDescriptionTextArea = new TextArea();
        stepDescriptionTextArea.setPromptText("Введите описание этапа...");

        stepNode.getChildren().addAll(stepImageView, stepDescriptionTextArea, chooseImageForStepButton);
        stepsVBox.getChildren().add(stepNode);
        stepNodes.add(stepNode);
    }

    private void removeStep() {
        int numSteps = stepsVBox.getChildren().size();

        if (numSteps > 0) {
            stepsVBox.getChildren().remove(numSteps - 1);
        }
    }

    private void chooseImageForStep(ImageView stepImageView) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Изображения", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Все файлы", "*.*")
        );

        Stage stage = (Stage) chooseImageBtn.getScene().getWindow();
        selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString();

            stepImageView.setFitWidth(200);
            stepImageView.setFitHeight(150);
            stepImageView.setImage(new Image(imagePath));
        }
    }

    private void updateRatingLabel(String selectedRating) {
        ratingLabel.setText(selectedRating);
    }

    @FXML
    private void handleTimeSelection() {
        int selectedHours = hoursSpinner.getValue();
        int selectedMinutes = minutesSpinner.getValue();

        String selectedTime = String.format("%02d:%02d", selectedHours, selectedMinutes);
        selectedTimeTextField.setText(selectedTime);
    }

    @FXML
    void ChooseImageForDish(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Выберите изображение");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Изображения", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Все файлы", "*.*")
        );

        Stage stage = (Stage) chooseImageBtn.getScene().getWindow();
        selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String imagePath = selectedFile.toURI().toString();

            choosenImage.setFitWidth(200);
            choosenImage.setFitHeight(150);

            choosenImage.setImage(new Image(imagePath));
        }
    }

    private String getImageFileName(ImageView imageView) {
        Image image = imageView.getImage();
        String imagePath = image.getUrl();

        int index = imagePath.lastIndexOf("/");
        if (index != -1) {
            return imagePath.substring(index + 1);
        } else {
            return UUID.randomUUID().toString() + ".png";
        }
    }

    @FXML
    void CreateNewDish(ActionEvent event) {
        stepsData.clear();

        String name = dishNameField.getText();
        String mainInfo = descriptionArea.getText();
        String category = categoryCondition.getText();
        int time = hoursSpinner.getValue() * 60 + minutesSpinner.getValue();
        Image mainImage = choosenImage.getImage();  //Должна быть ссылка на файл и сохранение в папку этого файла
        List<Product> productList = new ArrayList<>();

        for(String ingrid : selectedIngredients){
            ProductPattern productPattern = new DBAllProducts().Read(ingrid);
            Product prod = new Product(productPattern.name, productPattern.getProtein(), productPattern.getFat(), productPattern.getCarbohydrate(), 100);
            productList.add(prod);
        };

        float rating = 3.0f;
        switch (ratingLabel.getText()){
            case "*":
                rating = 1.0f;
                break;
            case "**":
                rating = 2.0f;
                break;
            case "***":
                rating = 3.0f;
                break;
            case "****":
                rating = 4.0f;
                break;
            case "*****":
                rating = 5.0f;
                break;
        }

        List<String> StepDescription = new ArrayList<String>();
        List<String> StepImage = new ArrayList<String>();

        for (Node stepNode : stepNodes) {
            if (stepNode instanceof VBox) {
                VBox vbox = (VBox) stepNode;
                ImageView stepImageView = (ImageView) vbox.getChildren().get(0);
                TextArea stepDescriptionTextArea = (TextArea) vbox.getChildren().get(1);

                String stepFileName = getImageFileName(stepImageView);

                copyImage(selectedFile, Paths.get("src/main/resources/img", "StageRecipe"), stepFileName);

                stepsData.add(new StepData(stepDescriptionTextArea.getText(), stepFileName));

                System.out.println("Main Image Path: " + "StageRecipe/" + stepFileName);
            }
        }

        int numSteps = stepsVBox.getChildren().size();
        for(int i = 0; i<numSteps; i++){
            StepDescription.add(stepsData.get(i).getStepDescription());
            StepImage.add(stepsData.get(i).getStepImagePath());   //Причем сохранять изображения тоже надо
        }

        String mainImageFileName = getImageFileName(choosenImage);

        copyImage(selectedFile, Paths.get("src/main/resources/img", "MainImage"), mainImageFileName);

        //System.out.println("Main Image Path: " + "MainImage/" + mainImageFileName);
        //System.out.println("Steps Data: " + stepsData.size());

        Recipe newRec = new Recipe(name, mainInfo, category, time, mainImageFileName, productList, rating, StepImage, StepDescription);

        new DBAllRecipes().Write(newRec);
        FoodViewController.thisRecipes = new DBAllRecipes().ReadAll();
        FoodViewController.recentlyAdded = new ArrayList<DishCard>(FoodViewController.CreateDishCardList(FoodViewController.thisRecipes));
        //new FoodViewController().updateScrollPane(FoodViewController.recentlyAdded);
       // Stage stage = (Stage) createDishBtn.getScene().getWindow();
        //stage.close();
    }

    private void copyImage(File selectedFile, Path destinationFolder, String fileName) {
        try {
            Path imagePath = destinationFolder.resolve(fileName);

            Files.copy(selectedFile.toPath(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ShowPreview(ActionEvent event) {

    }
}
