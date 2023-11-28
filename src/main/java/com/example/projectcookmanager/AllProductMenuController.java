package com.example.projectcookmanager;

import com.example.projectcookmanager.DataBases.DBAllProducts;
import com.example.projectcookmanager.Entity.Product;
import com.example.projectcookmanager.Entity.ProductPattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    private ObservableList<HBox> IngridLines = FXCollections.observableArrayList();

    private List<Product> products;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<ProductPattern> patternList = new ArrayList<>();
        patternList = new DBAllProducts().ReadAll();
        for(ProductPattern prod : patternList){
            AddIngrid(prod);
        }
    }

    private void AddIngrid(ProductPattern prod){
        TextField name = new TextField();
        name.setPromptText("Наименование ингридиента");
        name.setPrefWidth(155);
        name.setText(String.valueOf(prod.name));
        name.setId(prod.name);

        TextField prot = new TextField();
        prot.setPromptText("Б");
        prot.setPrefWidth(35);
        prot.setText(String.valueOf(prod.getProtein()));

        TextField fat = new TextField();
        fat.setPromptText("Ж");
        fat.setPrefWidth(35);
        fat.setText(String.valueOf(prod.getFat()));

        TextField car = new TextField();
        car.setPromptText("У");
        car.setPrefWidth(35);
        car.setText(String.valueOf(prod.getCarbohydrate()));

        Button rem = new Button("-");
        rem.setId(String.valueOf(IngridLines.size()));
        rem.setPrefWidth(20);
        rem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                HBox hBox = (HBox) rem.getParent();
                new DBAllProducts().Delete(hBox.getChildren().get(0).getId());
                IngridLines.remove(Integer.parseInt(rem.getId()));

                int i =0;
                for(HBox hb : IngridLines){
                    hb.getChildren().get(4).setId(String.valueOf(i));
                    i++;
                }
                ingridListView.setItems(IngridLines);
            }
        });

        HBox line = new HBox(name, prot, fat, car, rem);
        line.setPrefHeight(25);
        line.setMaxWidth(300);

        IngridLines.add(line);
        ingridListView.setItems(IngridLines);
    }

    @FXML
    void AddAllIngrid(ActionEvent event){
        List<ProductPattern> gg = new ArrayList<>();
        for(HBox hbox : IngridLines){
            TextField name = (TextField) hbox.getChildren().get(0);
            TextField prot = (TextField) hbox.getChildren().get(1);
            TextField fat = (TextField) hbox.getChildren().get(2);
            TextField car = (TextField) hbox.getChildren().get(3);

            ProductPattern productNew = new ProductPattern(name.getText(), Float.parseFloat(prot.getText()), Float.parseFloat(fat.getText()), Float.parseFloat(car.getText()));
            new DBAllProducts().Update(name.getId(), productNew);
        }
        NewReceiptCardController.newReceiptCardController.handleIngredientsMenu();
        Stage stage = (Stage) ingridListView.getScene().getWindow();
        stage.close();
    }

}
