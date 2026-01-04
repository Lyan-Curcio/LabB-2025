package com.lab_b.client;

import com.lab_b.client.controller.App; // QUESTO IMPORT MANCAVA
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class ClientApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Inizializziamo la classe di utilità App con lo Stage principale
        App app = App.getInstance();
        app.setStage(stage);
        
        // Carichiamo la prima scena (Benvenuto)
        // Assicurati che Benvenuto.fxml sia in src/main/resources/com/lab_b/
        app.changeScene("Benvenuto.fxml");
        
        stage.setTitle("Book Recommender");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}