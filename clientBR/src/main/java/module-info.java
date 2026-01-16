module com.bookrecommender.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.bookrecommender.common;
    requires java.rmi;
    requires java.desktop;
    requires java.sql;

    opens com.bookrecommender.client to javafx.fxml;
    opens com.bookrecommender.client.controller to javafx.fxml;
    exports com.bookrecommender.client;
}