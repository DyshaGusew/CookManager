package com.example.projectcookmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NoIngredientsController {
    private ObservableList<String> noIngridList= FXCollections.observableArrayList();
    private ObservableList<String> noMassList= FXCollections.observableArrayList();

    @FXML
    private ListView ListNoIngrid;

    @FXML
    private ListView ListNoMass;

    @FXML
    public void SetNoIngridList(List<String> noIngridList1) {
        for(String ingrid : noIngridList1) {
            noIngridList.add(ingrid);
        }

        ListNoIngrid.setItems(noIngridList);
    }

    @FXML
    public void SetNoMassList(List<Float> noMassList1) {
        for(Float mass : noMassList1) {
            noMassList.add(Float.toString(mass) + " гр");
        }

        ListNoMass.setItems(noMassList);
    }


    @FXML
    void AddNoIngrid(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewProductMenu.fxml"));
            Parent root = loader.load();
            NewProductMenuController controller = loader.getController();

            List<String> noIngString = new ArrayList<>();
            for(String name : noIngridList) {
                noIngString.add(name);
            }

            controller.SetData(noIngString);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
