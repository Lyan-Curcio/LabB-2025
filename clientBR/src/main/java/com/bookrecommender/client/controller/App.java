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
 * Classe singleton principale dell'applicazione client.
 * <p>
 * Si occupa di:
 * <ul>
 *   <li>Gestire l'accesso ai servizi remoti</li>
 *   <li>Memorizzare lo {@link Stage} di JavaFX e gestire la navigazione tra scene</li>
 * </ul>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class App {

    /**
     * Istanza singleton dell'applicazione.
     */
    private static App instance;

    /**
     * Servizio RMI per l'accesso pubblico al repository dei libri.
     */
    public final BookRepositoryService bookRepository;

    /**
     * Servizio RMI per l'accesso autenticato al repository dei libri.
     * Viene inizializzato dopo il login o registrazione.
     */
    public AuthedBookRepositoryService authedBookRepository;

    /**
     * Stage principale dell'applicazione JavaFX.
     */
    private Stage stage;

    /**
     * Costruttore privato.
     * <p>
     *   Inizializza la connessione al registry RMI e recupera il servizio {@link BookRepositoryService}.
     *   In caso di errore l'applicazione viene terminata.
     * </p>
     */
    private App() {
        BookRepositoryService _bookRepository = null;
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            // Lookup del servizio RMI (lancia eccezione se non presente)
            _bookRepository = (BookRepositoryService)
                registry.lookup("BookRecommenderService");
        }
        catch (RemoteException e) {
            System.err.println(
                "RemoteException nel recupero del servizio `BookRecommender` nel registry!"
            );
            System.exit(1);
        }
        catch (NotBoundException e) {
            System.err.println(
                "Impossibile trovare il servizio `BookRecommenderService` nel registry!"
            );
            System.exit(1);
        }

        bookRepository = _bookRepository;
    }

    /**
     * Imposta lo {@link Stage} principale dell'applicazione.
     *
     * @param s lo stage JavaFX principale
     */
    public void setStage(Stage s) {
        stage = s;
    }

    /**
     * Cambia la scena caricando un file FXML.
     *
     * @param fxml nome del file FXML da caricare
     */
    public void changeScene(String fxml) {
        // Carica il file FXML dalla cartella resources/com/lab_b/
        FXMLLoader loader =
            new FXMLLoader(getClass().getResource("/com/lab_b/" + fxml));
        Parent root;

        try {
            root = loader.load();
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (stage.getScene() == null) {
            // Prima inizializzazione della scena
            Scene scene = new Scene(root, 900, 600); // Dimensioni di default
            scene.getStylesheets().add(
                getClass()
                    .getResource("/com/lab_b/style.css")
                    .toExternalForm()
            );
            stage.setScene(scene);
            stage.setResizable(false);
        } else {
            // Cambio della root della scena esistente
            stage.getScene().setRoot(root);
        }
    }

    /**
     * Restituisce l'istanza singleton dell'applicazione.
     *
     * @return istanza unica di {@link App}
     */
    public static synchronized App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }
}
