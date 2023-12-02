package com.example.projectcookmanager;

import com.example.projectcookmanager.DataBases.DBAllProducts;
import com.example.projectcookmanager.Entity.ProductPattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class NewProductMenuController {

    @FXML
    private ListView newIngridListView;

    private ObservableList<HBox> newIngridLines = FXCollections.observableArrayList();

    @FXML
    void AddIngrid(ActionEvent event) {
        TextField name = new TextField();
        name.setPromptText("Наименование ингридиента");
        name.setPrefWidth(155);

        SetBJU(name);
    }

    private void SetBJU(TextField name) {
        TextField prot = new TextField();
        prot.setPromptText("Б");
        prot.setPrefWidth(35);

        TextField fat = new TextField();
        fat.setPromptText("Ж");
        fat.setPrefWidth(35);

        TextField car = new TextField();
        car.setPromptText("У");
        car.setPrefWidth(35);

        Button rem = new Button("-");
        rem.setId(String.valueOf(newIngridLines.size()));
        rem.setPrefWidth(20);
        rem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                newIngridLines.remove(Integer.parseInt(rem.getId()));
                int i =0;
                for(HBox hb : newIngridLines){
                    hb.getChildren().get(4).setId(String.valueOf(i));
                    i++;
                }
                newIngridListView.setItems(newIngridLines);
            }
        });

        HBox line = new HBox(name, prot, fat, car, rem);
        line.setPrefHeight(25);
        line.setMaxWidth(300);

        newIngridLines.add(line);
        newIngridListView.setItems(newIngridLines);
    }

    @FXML
    void AddAllIngrid(ActionEvent event) {
        List<ProductPattern> patternList = new ArrayList<>();
        for(HBox hbox : newIngridLines) {
            TextField name = (TextField) hbox.getChildren().get(0);
            TextField protein = (TextField) hbox.getChildren().get(1);
            TextField fat = (TextField) hbox.getChildren().get(2);
            TextField car = (TextField) hbox.getChildren().get(3);

            ProductPattern product = new ProductPattern(name.getText(), Float.parseFloat(protein.getText()),
                    Float.parseFloat(fat.getText()), Float.parseFloat(car.getText()));
            patternList.add(product);
            new DBAllProducts().Write(product);
        }
        NewReceiptCardController.newReceiptCardController.addIngredientsMenu(patternList);
        Stage stage = (Stage) newIngridListView.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void SetData(List<String> names) {
        for(String name : names) {
            TextField ingridNameView = new TextField();
            ingridNameView.setText(name);
            ingridNameView.setPrefWidth(155);

            SetBJU(ingridNameView);
        }
    }
}
