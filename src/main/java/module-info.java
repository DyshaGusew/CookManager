module com.example.projectcookmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.projectcookmanager to javafx.fxml;
    exports com.example.projectcookmanager;
}