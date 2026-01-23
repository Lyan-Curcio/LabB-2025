module com.bookrecommender.clientBR {
    requires java.rmi;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires com.bookrecommender.common;
    requires java.desktop;

    opens com.bookrecommender.client to javafx.fxml;
    opens com.bookrecommender.client.controller to javafx.fxml;

    exports com.bookrecommender.client;
}