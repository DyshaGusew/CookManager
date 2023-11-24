package com.example.projectcookmanager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.List;

public class NoIngredientsController {

    private ObservableList<String> noIngridList= FXCollections.observableArrayList();
    private ObservableList<String> noMassList= FXCollections.observableArrayList();

    @FXML
    private ListView ListNoIngrid;

    @FXML
    private ListView ListNoMass;

    @FXML
    public void setNoIngridList(List<String> noIngridList1) {
        for(String ingrid : noIngridList1){
            noIngridList.add(ingrid);
        }

        ListNoIngrid.setItems(noIngridList);
    }
    @FXML
    public void setNoMassList(List<Float> noMassList1) {
        for(Float mass : noMassList1){
            noMassList.add(Float.toString(mass) + " гр");
        }

        ListNoMass.setItems(noMassList);
    }
}
