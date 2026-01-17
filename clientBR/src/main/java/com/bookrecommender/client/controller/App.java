package com.bookrecommender.client.controller;

import com.bookrecommender.common.AuthedBookRepositoryService;
import com.bookrecommender.common.BookRepositoryService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Classe di supporto per la navigazione tra le scene.
 * I metodi return servono a tornare alle schermate precedenti.
 */
public class App {

    private static App instance;
    public final BookRepositoryService bookRepository;
    public AuthedBookRepositoryService authedBookRepository;
    private Stage stage;

    private App() {
        BookRepositoryService _bookRepository = null;
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            _bookRepository = (BookRepositoryService) registry.lookup("BookRecommenderService"); // Per triggerare l'eccezione in assenza del servizio
        }
        catch (RemoteException e) {
            System.err.println("RemoteException nel recupero del servizio `BookRecommender` nel registry!");
            System.exit(1);
        }
        catch (NotBoundException e) {
            System.err.println("Impossibile trovare il servizio `BookRecommenderService` nel registry!");
            System.exit(1);
        }

        bookRepository = _bookRepository;
    }

    public void setStage(Stage s) {
        stage = s;
    }

    public void changeScene(String fxml) {
        // Carica il file FXML dalla cartella resources/com/lab_b/
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/lab_b/" + fxml));
        Parent root;
        try
        {
            root = loader.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }

        if (stage.getScene() == null) {
            stage.setScene(new Scene(root, 900, 600)); // Dimensioni default
        } else {
            stage.getScene().setRoot(root);
        }
    }

    public static synchronized App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }
}