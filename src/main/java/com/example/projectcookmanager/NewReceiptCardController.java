package com.example.projectcookmanager;

import DishModel.DishCard;
import DishModel.StepData;
import com.example.projectcookmanager.DataBases.DBAllProducts;
import com.example.projectcookmanager.DataBases.DBAllRecipes;
import com.example.projectcookmanager.Entity.Product;
import com.example.projectcookmanager.Entity.ProductPattern;
import com.example.projectcookmanager.Entity.Recipe;
import com.example.projectcookmanager.Parser.Parser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
    static public NewReceiptCardController newReceiptCardController;
    private FoodViewController foodViewController;

    public void setFoodViewController(FoodViewController foodViewController) {
        this.foodViewController = foodViewController;
    }

    private FullReceiptCardController fullReceiptCardController;

    public void setFullReceiptCardController(FullReceiptCardController fullReceiptCardController) {
        this.fullReceiptCardController = fullReceiptCardController;
    }

    private List<String> selectedIngredients = new ArrayList<>();

    private List<String> selectedMass = new ArrayList<>();

    private List<File> selectedFilesList = new ArrayList<>();

    private List<StepData> stepsData = new ArrayList<>();

    private ObservableList<String> ingredientNames = FXCollections.observableArrayList();

    private ObservableList<TextField> ingredientMass = FXCollections.observableArrayList();

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
    private ScrollPane stepsScroll;

    @FXML
    private ScrollPane stepsScrollInfo;

    @FXML
    private TextField dishNameField;

    @FXML
    private ImageView choosenImage;

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
    private ListView listIngredients;

    @FXML
    private ListView listMassIngredients;

    @FXML
    private TextField recipeUrlArea;

    private DBAllRecipes dbAllRecipes = new DBAllRecipes();

    @FXML
    private ImageView stepImageView;

    @FXML
    private Button chooseImageForStepButton;

    private List<Node> stepNodes = new ArrayList<>();

    private File selectedFile;

    private File selectedFileMain;

    private Recipe parceRecipe;

    @FXML
    void initialize() {
        SaveCategoryCondition();

        ChooseTimeOfCooking();

        SaveRatingCondition();

        handleIngredientsMenu();

        hoursSpinner.valueProperty().addListener((obs, oldValue, newValue) -> handleTimeSelection());
        minutesSpinner.valueProperty().addListener((obs, oldValue, newValue) -> handleTimeSelection());
    }

    //Создание вручную работа со всеми элементами
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

    public void handleIngredientsMenu() {
        listIngredients.getItems().clear();
        listMassIngredients.getItems().clear();
        dishIngredientsMenu.getItems().clear();

        DBAllProducts dbAllProducts = new DBAllProducts();
        List<ProductPattern> allProducts = dbAllProducts.ReadAll();

        for (ProductPattern productPattern : allProducts) {
            CheckMenuItem checkmenuItem = new CheckMenuItem(productPattern.name);

            checkmenuItem.setOnAction(event -> handleIngredientSelection(checkmenuItem));

            dishIngredientsMenu.getItems().add(checkmenuItem);
        }
    }

    public void addIngredientsMenu(List<ProductPattern> prods) {
        for (ProductPattern productPattern : prods) {
            CheckMenuItem checkmenuItem = new CheckMenuItem(productPattern.name);

            checkmenuItem.setOnAction(event -> handleIngredientSelection(checkmenuItem));

            dishIngredientsMenu.getItems().add(checkmenuItem);
        }
    }

    private void handleIngredientSelection(CheckMenuItem selectedMenuItem) {
        System.out.println("Selected ingredient: " + selectedMenuItem.getText());

        if (selectedMenuItem.isSelected()) {
            selectedIngredients.add(selectedMenuItem.getText());

            ingredientNames.add(selectedMenuItem.getText());

            TextField textMass = new TextField("0");
            textMass.setPrefWidth(10);
            textMass.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            textMass.setPrefHeight(5);
            textMass.setId(selectedMenuItem.getText());
            ingredientMass.add(textMass);

            listIngredients.setItems(ingredientNames);
            listMassIngredients.setItems(ingredientMass);
        } else {
            ingredientNames.remove(selectedMenuItem.getText());

            for(TextField textField : ingredientMass){
                if(textField.getId().equals(selectedMenuItem.getText())){
                    ingredientMass.remove(textField);
                    break;
                }
            }

            listIngredients.setItems(ingredientNames);
            listMassIngredients.setItems(ingredientMass);

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

    @FXML
    void AddStep(ActionEvent eventss) {
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

    @FXML
    void RemoveStep(ActionEvent event) {
        int numSteps = stepsVBox.getChildren().size();

        if (numSteps > 0) {
            stepsVBox.getChildren().remove(numSteps - 1);

            if (!stepNodes.isEmpty()) {
                stepNodes.remove(stepNodes.size() - 1);
            }
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
        time  = selectedHours*60 + selectedMinutes;
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


    //Создание новой карточки
    @FXML
    void CreateNewDish(ActionEvent event) throws IOException {
        List<Product> productList = new ArrayList<>();
        stepsData.clear();

        List<TextField> textFields = listMassIngredients.getItems();
        int i_ = 0;
        for (String ingrid : selectedIngredients) {
            ProductPattern productPattern = new DBAllProducts().Read(ingrid);
            Product prod = new Product(productPattern.name, productPattern.getProtein(), productPattern.getFat(), productPattern.getCarbohydrate(), Float.parseFloat(textFields.get(i_).getText()));
            productList.add(prod);
            i_++;
        }

        if(choosenImage == null){
            if (selectedFile == null || selectedFileMain == null) {
                System.out.println("Файл не выбран.");
                return;
            }
        }

        System.out.println("Dish Name: " + dishNameField.getText());
        System.out.println("Category: " + categoryCondition.getText());
        System.out.println("Description: " + descriptionArea.getText());
        System.out.println("Selected Rating: " + selectedRating);
        System.out.println("Selected Time: " + timeOfCookingMenuBtn);
        System.out.println("Steps VBox Children: " + stepsVBox.getChildren().isEmpty());
        //System.out.println("Steps Sroll Children: " + stepsScroll.getContent());
        System.out.println("ChoosenImage +" + choosenImage.getImage());
        System.out.println("Ingredients +" + listIngredients.getItems());

        if (dishNameField.getText().trim().isEmpty() || categoryCondition.getText().trim().isEmpty()
                || descriptionArea.getText().trim().isEmpty() || listIngredients.getItems().isEmpty() ||
                selectedRating == null || timeOfCookingMenuBtn == null || stepsVBox.getChildren().isEmpty()) {
            System.out.println("Заполните все поля.");
            return;
        }

        if(selectedFilesList.size() != 0){
            for (int i = 0; i < stepNodes.size(); i++) {
                Node stepNode = stepNodes.get(i);
                if (stepNode instanceof VBox) {
                    VBox vbox = (VBox) stepNode;
                    ImageView stepImageView = (ImageView) vbox.getChildren().get(0);
                    TextArea stepDescriptionTextArea = (TextArea) vbox.getChildren().get(1);

                    File stepFile = selectedFilesList.get(i);

                    if (stepFile != null && (!stepImageView.getImage().isError()
                            || !stepDescriptionTextArea.getText().isEmpty())) {
                        String originalFileName = stepFile.getName();
                        String fileExtension = originalFileName.substring
                                (originalFileName.lastIndexOf(".") + 1);

                        stepFileName = originalFileName.replace("." + fileExtension, "") + ".png";

                        copyImageAsync(stepFile, Paths.get("src/main/resources/img/StageRecipe"), stepFileName);

                        stepsData.add(new StepData(stepDescriptionTextArea.getText(), stepFileName));

                        System.out.println("Stage Image Path: " + "img/StageRecipe/" + stepFileName);
                    }
                }
            }
        }

        CompletableFuture<String> mainImageFuture = null;
        CompletableFuture<Void> stepImageFuture = null;
        if(selectedFileMain != null){
            mainImageFuture = createMainImageAsync(selectedFileMain);

            stepImageFuture = createStepImagesAsync(selectedFilesList);

            mainImageFuture.join();
            stepImageFuture.join();
        }

        List<String> StepDescription = new ArrayList<>();
        List<String> StepImage = new ArrayList<>();

        for (StepData stepData : stepsData) {
            StepDescription.add(stepData.getStepDescription());
            StepImage.add(stepData.getStepImagePath());
        }

        Recipe newRecipe;
        if(mainImageFuture != null){
            newRecipe = new Recipe(
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
        }
        else{
            newRecipe = new Recipe(
                    dishNameField.getText(),
                    descriptionArea.getText(),
                    categoryCondition.getText(),
                    time,
                    parceRecipe.getMainImageLink(),
                    productList,
                    setRating(ratingLabel),
                    parceRecipe.getImagesStageLinks(),
                    parceRecipe.getTextStages());
        }



        if(new DBAllRecipes().Read(newRecipe.name) != null){
            new DBAllRecipes().Delete(newRecipe.name);
        }
        new DBAllRecipes().Write(newRecipe);
        FoodViewController.foodViewController.updateDishCards();
        DishCard dish = new DishCard();
        dish.setCalories(newRecipe.getCalories());
        dish.setName(newRecipe.name);
        dish.setTime(newRecipe.getTimeCooking());
        dish.setImageUrl(newRecipe.getMainImageLink());
        dish.setRatingUrl(newRecipe.getRating());
        FullReceiptCardController.fullReceiptCardController.setData(dish);
        closeWindow();
    }


    //Добавление с сайта
    @FXML
    void AddOfSite(ActionEvent event){
        if (!recipeUrlArea.getText().equals("")) {
            parceRecipe = Parser.RecOfParser(recipeUrlArea.getText());
            setData(parceRecipe);
            if(Parser.noIngrid.size() != 0){
                openInfoAboutNoIngrid(Parser.noIngrid, Parser.noMass);
            }
        }
    }

    //Установка данных в зависимости от рецепта
    private void setData(Recipe recipe){
        dishNameField.setText(recipe.name);
        categoryCondition.setText(recipe.getCategory());

        descriptionArea.setText(recipe.getMainInfo());

        selectedRating = getStringRating(recipe.getRating());
        ratingLabel.setText(selectedRating);

        selectedTime = String.format("%02d:%02d", recipe.getTimeCooking()/60, recipe.getTimeCooking()%60);
        timeOfCookingMenuBtn.setText(selectedTime);
        time = recipe.getTimeCooking();

        setIngridMass(recipe);

        InputStream imageStream = getClass().getResourceAsStream("/img/MainImage/" + recipe.getMainImageLink());
        choosenImage.setImage(new Image(imageStream));

        for(int i = 0; i < recipe.getTextStages().size(); i++){
            setStep(recipe.getTextStages().get(i), recipe.getImagesStageLinks().get(i));
        }
    }

    private void setStep(String textInfo, String imageLink) {
        VBox stepNode = new VBox();

        ImageView stepImageView = new ImageView();
        stepImageView.setFitWidth(250);
        stepImageView.setFitHeight(200);
        InputStream imageStream = getClass().getResourceAsStream("/img/StageRecipe/" + imageLink);
        stepImageView.setImage(new Image(imageStream));

        TextArea stepDescriptionTextArea = new TextArea();
        stepDescriptionTextArea.setMaxWidth(400);
        stepDescriptionTextArea.setMaxHeight(Region.USE_COMPUTED_SIZE);
        stepDescriptionTextArea.setFont(new Font("Arial Black", 14));
        stepDescriptionTextArea.setWrapText(true);
        stepDescriptionTextArea.setText(textInfo);

        stepNode.getChildren().addAll(stepImageView, stepDescriptionTextArea);
        stepsVBox.getChildren().add(stepNode);
        stepNodes.add(stepNode);
    }

    private void setIngridMass(Recipe recipe) {
        dishIngredientsMenu.getItems().clear();
        DBAllProducts dbAllProducts = new DBAllProducts();
        List<ProductPattern> allProducts = dbAllProducts.ReadAll();

        for (ProductPattern productPattern : allProducts) {
            CheckMenuItem checkmenuItem = new CheckMenuItem(productPattern.name);

            checkmenuItem.setOnAction(event -> handleIngredientSelection(checkmenuItem));
            for (Product product : recipe.getProducts()){
                if(product.name.equals(productPattern.name)){
                    checkmenuItem.setSelected(true);
                    selectedIngredients.add(product.name);

                    ingredientNames.add(product.name);

                    TextField textMass = new TextField(Float.toString(product.getMass()));
                    textMass.setPrefWidth(10);
                    textMass.setFont(Font.font("Arial", FontWeight.BOLD, 10));
                    textMass.setPrefHeight(5);
                    textMass.setId(product.name);
                    ingredientMass.add(textMass);

                    listIngredients.setItems(ingredientNames);
                    listMassIngredients.setItems(ingredientMass);
                    break;
                }
            }
            dishIngredientsMenu.getItems().add(checkmenuItem);
        }
    }

    //Установка данных где-либо
    public void InitData(DishCard dish) throws IOException {
        parceRecipe = new DBAllRecipes().Read(dish.getName());
        setData(parceRecipe);
    }


    //Работа с продуктами
    @FXML
    void AddNewProduct(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewProductMenu.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ShowAllProducts(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AllProductsList.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openInfoAboutNoIngrid(List<String> noIngredients, List<Float> noMass){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NoIngredients.fxml"));
            Parent root = loader.load();

            NoIngredientsController controller = loader.getController();
            controller.setNoIngridList(noIngredients);
            controller.setNoMassList(noMass);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Работа с рейтингом
    private float setRating(Label ratingLabel) {
        rating = ratingLabel.getText().length();
        return  rating;
    }

    private String getStringRating(float rating){
        String ratingString = "";

        for (int i = 0; i < Math.round(rating); i++) {
            ratingString += "*";
        }

        return ratingString;
    }


    //Работа с картинками
    private CompletableFuture<String> createMainImageAsync(File selectedFileMain) {
        return CompletableFuture.supplyAsync(() -> {
            String originalMainFileName = selectedFileMain.getName();
            String mainFileExtension = originalMainFileName.substring
                    (originalMainFileName.lastIndexOf(".") + 1);

            String mainImageFileName = originalMainFileName.replace
                    ("." + mainFileExtension, "") + ".jpg";

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
                    String fileExtension = originalFileName.substring
                            (originalFileName.lastIndexOf(".") + 1);

                    String stepFileName = originalFileName.replace
                            ("." + fileExtension, "") + ".jpg";

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

    //Закрытие окна
    private void closeWindow() {
        Stage stage = (Stage) createDishBtn.getScene().getWindow();
        stage.close();
    }
}
