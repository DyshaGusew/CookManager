package com.example.projectcookmanager;

import com.example.projectcookmanager.DataBases.DBAllProducts;
import com.example.projectcookmanager.DataBases.DBRecConnectProd;
import com.example.projectcookmanager.Entity.ProductPattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AllProductMenuController implements Initializable {

    @FXML
    private ListView ingridListView;

    private ObservableList<HBox> ingridLines = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<ProductPattern> patternList;
        patternList = new DBAllProducts().ReadAll();
        for(ProductPattern prod : patternList){
            addIngrid(prod);
        }
    }

    private void addIngrid(ProductPattern prod){
        TextField name = new TextField();
        name.setPromptText("Наименование ингридиента");
        name.setPrefWidth(155);
        name.setText(String.valueOf(prod.name));
        name.setId(prod.name);

        TextField prot = createBJU("Б", 35, prod.getProtein());

        TextField fat = createBJU("Ж", 35, prod.getFat());

        TextField car = createBJU("У", 35, prod.getCarbohydrate());

        Button remove = new Button("-");
        remove.setId(String.valueOf(ingridLines.size()));
        remove.setPrefWidth(20);
        remove.setOnAction(event ->  setIngridInListView(remove));

        HBox line = new HBox(name, prot, fat, car, remove);
        line.setPrefHeight(25);
        line.setMaxWidth(300);

        ingridLines.add(line);
        ingridListView.setItems(ingridLines);
    }

    private void setIngridInListView(Button btn){
        HBox hBox = (HBox) btn.getParent();

        new DBRecConnectProd().DeleteProd(new DBAllProducts().Read(hBox.getChildren().get(0).getId()).id);
        new DBAllProducts().Delete(hBox.getChildren().get(0).getId());

        ingridLines.remove(Integer.parseInt(btn.getId()));

        int i = 0;
        for(HBox hb : ingridLines){
            hb.getChildren().get(4).setId(String.valueOf(i));
            i++;
        }
        ingridListView.setItems(ingridLines);
    }
    private TextField createBJU(String name, int wight, float value) {
        TextField textField = new TextField();
        textField.setPromptText(name);
        textField.setPrefWidth(wight);
        textField.setText(String.valueOf(value));

        return textField;
    }

    @FXML
    void addAllIngrid(ActionEvent event){
        List<ProductPattern> gg = new ArrayList<>();
        for(HBox hbox : ingridLines){
            TextField name = (TextField) hbox.getChildren().get(0);
            TextField protein = (TextField) hbox.getChildren().get(1);
            TextField fat = (TextField) hbox.getChildren().get(2);
            TextField car = (TextField) hbox.getChildren().get(3);


            ProductPattern productNew = new ProductPattern(name.getText(), Float.parseFloat(protein.getText()),
                    Float.parseFloat(fat.getText()), Float.parseFloat(car.getText()));

            new DBAllProducts().Update(name.getId(), productNew);
            gg.add(productNew);
        }

        NewReceiptCardController.newReceiptCardController.handleIngredientsMenu();

        Stage stage = (Stage) ingridListView.getScene().getWindow();
        stage.close();
    }
}
