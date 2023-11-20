package com.example.projectcookmanager;

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

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NewReceiptCardController {
    private FoodViewController foodViewController;

    public void setFoodViewController(FoodViewController foodViewController) {
        this.foodViewController = foodViewController;
    }

    private List<String> selectedIngredients = new ArrayList<>();

    private List<File> selectedFilesList = new ArrayList<>();

    private List<StepData> stepsData = new ArrayList<>();
    private String selectedTime;
    private int time;
    private String selectedRating;

    private float rating;
    private String stepFileName;

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

    private File selectedFileMain;

    @FXML
    void initialize() {
        SaveCategoryCondition();

        ChooseTimeOfCooking();

        SaveRatingCondition();

        handleIngredientsMenu();

        addStepButton.setOnAction(event -> addStep());
        removeStepButton.setOnAction(event -> removeStep());
        hoursSpinner.valueProperty().addListener((obs, oldValue, newValue) -> handleTimeSelection());
        minutesSpinner.valueProperty().addListener((obs, oldValue, newValue) -> handleTimeSelection());
    }

    private void ChooseTimeOfCooking() {
        timeOfCookingMenuBtn.getItems().forEach(menuItem -> {
            if (menuItem instanceof CheckMenuItem) {
                CheckMenuItem checkMenuItem = (CheckMenuItem) menuItem;
                checkMenuItem.setOnAction(e -> {
                    if (checkMenuItem.getText().equals("В часах")) {
                        hoursSpinner.setVisible(true);
                        minutesSpinner.setVisible(false);
                        selectedTime = "В часах";
                    } else if (checkMenuItem.getText().equals("В минутах")) {
                        hoursSpinner.setVisible(false);
                        minutesSpinner.setVisible(true);
                        selectedTime = "В минутах";
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

        if (selectedMenuItem.isSelected()) {
            selectedIngredients.add(selectedMenuItem.getText());
        } else {
            if (selectedIngredients != null) {
                selectedIngredients.removeIf(el -> el.equals(selectedMenuItem.getText()));
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
                menuItem.setOnAction(e -> {
                    String rating = menuItem.getText();
                    updateRatingLabel(rating);
                    selectedRating = rating;
                });
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
            selectedFilesList.add(selectedFile);

            try {
                InputStream imageStream = new FileInputStream(selectedFile);
                String imagePath = selectedFile.toURI().toString();

                ImageView newStepImageView = new ImageView();
                newStepImageView.setFitWidth(200);
                newStepImageView.setFitHeight(150);
                newStepImageView.setImage(new Image(imageStream));

                VBox parentVBox = (VBox) stepImageView.getParent();
                parentVBox.getChildren().set(0, newStepImageView);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateRatingLabel(String selectedRating) {
        ratingLabel.setText(selectedRating);
    }

    @FXML
    private void handleTimeSelection() {
        int selectedHours = hoursSpinner.getValue();
        int selectedMinutes = minutesSpinner.getValue();

        selectedTime = String.format("%02d:%02d", selectedHours, selectedMinutes);
        timeOfCookingMenuBtn.setText(selectedTime);
        time  = selectedHours + selectedMinutes;
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
        selectedFileMain = fileChooser.showOpenDialog(stage);

        if (selectedFileMain != null) {
            try {
                InputStream imageStream = new FileInputStream(selectedFileMain);
                String imagePath = selectedFileMain.toURI().toString();

                choosenImage.setFitWidth(200);
                choosenImage.setFitHeight(150);

                choosenImage.setImage(new Image(imageStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void CreateNewDish(ActionEvent event) {
        stepsData.clear();
        List<Product> productList = new ArrayList<>();

        for (String ingrid : selectedIngredients) {
            ProductPattern productPattern = new DBAllProducts().Read(ingrid);
            Product prod = new Product(productPattern.name, productPattern.getProtein(), productPattern.getFat(), productPattern.getCarbohydrate(), 100);
            productList.add(prod);
        }

        if (selectedFile == null || selectedFileMain == null) {
            System.out.println("Файл не выбран.");
            return;
        }

        if (dishNameField.getText().trim().isEmpty() || categoryCondition.getText().trim().isEmpty()
                || descriptionArea.getText().trim().isEmpty() ||
                selectedRating == null || selectedTime == null || stepsVBox.getChildren().isEmpty()) {
            System.out.println("Заполните все поля.");
            return;
        }

        for (int i = 0; i < stepNodes.size(); i++) {
            Node stepNode = stepNodes.get(i);
            if (stepNode instanceof VBox) {
                VBox vbox = (VBox) stepNode;
                ImageView stepImageView = (ImageView) vbox.getChildren().get(0);
                TextArea stepDescriptionTextArea = (TextArea) vbox.getChildren().get(1);

                File stepFile = selectedFilesList.get(i);

                if (stepFile != null && (!stepImageView.getImage().isError() || !stepDescriptionTextArea.getText().isEmpty())) {
                    String originalFileName = stepFile.getName();
                    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);

                    stepFileName = originalFileName.replace("." + fileExtension, "") + ".png";

                    copyImageAsync(stepFile, Paths.get("src/main/resources/img/StageRecipe"), stepFileName);

                    stepsData.add(new StepData(stepDescriptionTextArea.getText(), stepFileName));

                    System.out.println("Stage Image Path: " + "img/StageRecipe/" + stepFileName);
                }
            }
        }

        CompletableFuture<String> mainImageFuture = createMainImageAsync(selectedFileMain);

        CompletableFuture<Void> stepImageFuture = createStepImagesAsync(selectedFilesList);


        mainImageFuture.join();
        stepImageFuture.join();

        List<String> StepDescription = new ArrayList<>();
        List<String> StepImage = new ArrayList<>();

        for (StepData stepData : stepsData) {
            StepDescription.add(stepData.getStepDescription());
            StepImage.add(stepData.getStepImagePath());
        }

        Recipe newRecipe = new Recipe(
                dishNameField.getText(),
                descriptionArea.getText(),
                categoryCondition.getText(),
                time,
                mainImageFuture.join(),
                productList,
                setRating(ratingLabel),
                StepImage,
                StepDescription
        );

        new DBAllRecipes().Write(newRecipe);

        closeWindow();
    }

    private CompletableFuture<String> createMainImageAsync(File selectedFileMain) {
        return CompletableFuture.supplyAsync(() -> {
            String originalMainFileName = selectedFileMain.getName();
            String mainFileExtension = originalMainFileName.substring(originalMainFileName.lastIndexOf(".") + 1);

            String mainImageFileName = originalMainFileName.replace("." + mainFileExtension, "") + ".png";

            Path destinationFolder = Paths.get("target/classes/img/MainImage");
            createDirectoryIfNotExists(destinationFolder);

            copyImage(selectedFileMain, destinationFolder, mainImageFileName);

            System.out.println("Main Image Path: " + mainImageFileName);

            return mainImageFileName;
        });
    }

    private void copyImage(File selectedFile, Path destinationFolder, String fileName) {
        try {
            Path imagePath = destinationFolder.resolve(fileName);

            Files.copy(selectedFile.toPath(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CompletableFuture<Void> createStepImagesAsync(List<File> selectedFilesList) {
        return CompletableFuture.runAsync(() -> {
            Path destinationFolder = Paths.get("target/classes/img/StageRecipe");
            createDirectoryIfNotExists(destinationFolder);

            for (int i = 0; i < selectedFilesList.size(); i++) {
                File stepFile = selectedFilesList.get(i);

                if (stepFile != null) {
                    String originalFileName = stepFile.getName();
                    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);

                    String stepFileName = originalFileName.replace("." + fileExtension, "") + ".png";

                    copyImage(stepFile, destinationFolder, stepFileName);

                    System.out.println("Stage Image Path: " + "img/StageRecipe/" + stepFileName);
                }
            }
        });
    }

    private void createDirectoryIfNotExists(Path directoryPath) {
        try {
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private float setRating(Label ratingLabel)
    {
        switch (ratingLabel.getText()) {
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

        return  rating;
    }

    private void closeWindow() {
        Stage stage = (Stage) createDishBtn.getScene().getWindow();
        stage.close();
    }

    private CompletableFuture<Void> copyImageAsync(File selectedFile, Path destinationFolder, String fileName) {
        return CompletableFuture.runAsync(() -> {
            try {
                Path imagePath = destinationFolder.resolve(fileName);

                Files.copy(selectedFile.toPath(), imagePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    void ShowPreview(ActionEvent event) {

    }
}
