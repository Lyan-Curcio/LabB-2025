package com.bookrecommender.client;

import com.bookrecommender.client.controller.App;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * La classe {@code ClientApplication} è il punto di avvio per JavsFX.
 * Gestisce l'avvio dell'interfaccia grafica.
 * <p>
 *   All'avvio, inizializza l'istanza singleton di {@link App}, imposta lo {@link Stage} principale
 *   e carica la scena iniziale (Benvenuto.fxml).
 * </p>
 */
public class ClientApplication extends Application {

    /**
     * Metodo chiamato automaticamente da JavaFX all'avvio dell'applicazione.
     * Inizializza l'applicazione, configura lo {@link Stage} principale e carica
     * la scena iniziale definita in "Benvenuto.fxml".
     *
     * @param stage Lo stage principale fornito da JavaFX.
     * @throws IOException Se si verifica un errore durante il caricamento del file FXML.
     */
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

    /**
     * Punto di ingresso della classe {@link ClientApplication}.
     * Avvia il framework JavaFX e richiama il metodo {@link #start(Stage)}.
     *
     * @param args Argomenti della linea di comando (non utilizzati).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
