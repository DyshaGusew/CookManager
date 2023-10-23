module com.example.projectcookmanager {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.projectcookmanager to javafx.fxml;
    exports com.example.projectcookmanager;
}