module com.lab_b.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.lab_b.common;
    requires java.rmi;
    requires java.desktop;

    opens com.lab_b.client to javafx.fxml;
    opens com.lab_b.client.controller to javafx.fxml;
    exports com.lab_b.client;
}