package com.lab_b;

import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class ClientApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Inizializziamo la classe di utilità App con lo Stage principale
        App app = new App();
        app.setStage(stage);
        
        // Carichiamo la prima scena (Benvenuto)
        app.changeScene("Benvenuto.fxml");
        
        stage.setTitle("Book Recommender");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}