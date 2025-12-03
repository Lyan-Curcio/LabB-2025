package com.lab_b;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Classe di supporto per la navigazione tra le scene.
 */
public class App {

    private static Stage stage;

    public void setStage(Stage s) {
        stage = s;
    }

    public void changeScene(String fxml) throws IOException {
        // Carica il file FXML dalla cartella resources/com/lab_b/
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lab_b/" + fxml));
        Parent root = loader.load();
        
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root, 900, 600)); // Dimensioni default
        } else {
            stage.getScene().setRoot(root);
        }
    }
}