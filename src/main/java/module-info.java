module com.example.projectcookmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.projectcookmanager to javafx.fxml;
    exports com.example.projectcookmanager;
    exports com.example.projectcookmanager.DataBases;
    opens com.example.projectcookmanager.DataBases to javafx.fxml;
    exports com.example.projectcookmanager.Entity;
    opens com.example.projectcookmanager.Entity to javafx.fxml;
}