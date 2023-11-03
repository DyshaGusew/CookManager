package com.example.projectcookmanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Food-ReceiptNew.fxml"));
        stage.setTitle("Food Receipts");
        stage.setScene(new Scene(root, 930, 635));
        stage.show();
        stage.setResizable(false);
    }

    public static void main(String[] args) {
        launch();
    }
}