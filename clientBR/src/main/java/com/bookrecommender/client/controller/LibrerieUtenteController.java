package com.bookrecommender.client.controller;

import com.bookrecommender.common.dto.Library;
import com.bookrecommender.common.enums.library.CreateLibResult;
import com.bookrecommender.common.enums.library.DeleteLibResult;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Controller della schermata "Librerie Utente".
 * Gestisce la visualizzazione, creazione, eliminazione delle librerie associate all'utente.
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class LibrerieUtenteController {

    /** ListView che mostra le librerie dell'utente */
    @FXML
    private ListView<String> listaLibrerieUtente;

    /** Messaggio mostrato quando non si ha nessuna libreria */
    @FXML
    private Label listLabel;

    /** Label per mostrare errori o messaggi informativi all'utente */
    @FXML
    private Label errorLabel;

    /** Label per visualizzare il nome dell'utente loggato */
    @FXML
    private Label labelNomeUtente;

    /** Campo di testo per inserire il nome di una nuova libreria da creare */
    @FXML
    private TextField tfNomeNuovaLibreria;

    /** Pulsante per eliminare la libreria selezionata */
    @FXML
    private Button btnEliminaLibreria;

    /** Pulsante per aprire la libreria selezionata */
    @FXML
    private Button btnGuardaLibreria;

    /** Lista interna delle librerie dell'utente */
    private LinkedList<Library> librerie;

    /** HashMap per accedere alle librerie tramite il nome */
    private HashMap<String, Library> mapLibrerie =  new HashMap<>();

    /** Flag che indica se l'utente ha confermato l'eliminazione di una libreria */
    private boolean confermaEliminazione = false;

    /** Libreria attualmente selezionata dall'utente */
    public static Library libreria;

    /**
     * Inizializza il controller.
     * Imposta il nome utente, configura la ListView e i listener.
     *
     * @throws RemoteException se non riesce a comunicare con il repository remoto
     */
    @FXML
    private void initialize() throws RemoteException
    {
        // Imposta nome utente sidebar
        if (LoginController.user == Utente.REGISTRATO) {
            labelNomeUtente.setText("Utente: " + LoginController.userId);
        } else {
            labelNomeUtente.setText("Ospite");
        }

        // Imposta listener per ridimensionare il testo delle celle della lista
        listaLibrerieUtente.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setWrapText(true);
                    setPrefWidth(0);
                    maxWidthProperty().bind(listaLibrerieUtente.widthProperty().subtract(20));
                }
            }
        });

        ricaricaLista();

        // Listener per la selezione di una libreria
        listaLibrerieUtente.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1)
            {
                if(t1 != null) {
                    libreria = mapLibrerie.get(listaLibrerieUtente.getSelectionModel().getSelectedItem());
                    btnEliminaLibreria.setVisible(true);
                    btnGuardaLibreria.setVisible(true);
                    confermaEliminazione = false;
                    errorLabel.setText("");
                } else {
                    btnEliminaLibreria.setVisible(false);
                    btnGuardaLibreria.setVisible(false);
                }
            }
        });
    }

    /**
     * Ricarica la lista delle librerie dell'utente.
     * Mostra un messaggio se la lista è vuota.
     *
     * @throws RemoteException se non riesce a comunicare con il repository remoto
     */
    private void ricaricaLista() throws RemoteException {
        librerie = App.getInstance().authedBookRepository.getMyLibrerie();

        if(librerie.isEmpty()) {
            listLabel.setVisible(true);
            listaLibrerieUtente.setVisible(false); // Nascondi lista se vuota
        } else {
            listLabel.setVisible(false);
            listaLibrerieUtente.setVisible(true);

            listaLibrerieUtente.setItems(
                FXCollections.observableArrayList(
                    librerie.stream()
                        .map(l -> l.name)
                        .collect(Collectors.toList())
                )
            );

            mapLibrerie.clear();
            librerie.forEach(l -> mapLibrerie.put(l.name, l));
        }
    }

    /**
     * Gestisce la creazione di una nuova libreria.
     * Verifica che il campo nome non sia vuoto e mostra messaggi di errore o successo.
     *
     * @param event evento associato al click sul pulsante "Crea Libreria"
     * @throws RemoteException se non riesce a comunicare con il repository remoto
     */
    @FXML
    void btnCreaLibreria(ActionEvent event) throws RemoteException
    {
        errorLabel.setText("");
        String nomeLib = tfNomeNuovaLibreria.getText().trim();

        if(nomeLib.isEmpty())
        {
            errorLabel.setText("Inserisci il nome della libreria da creare.");
            return;
        }

        CreateLibResult result = App.getInstance().authedBookRepository.creaLibreria(nomeLib);

        if(result == CreateLibResult.OK)
        {
            tfNomeNuovaLibreria.setText(""); // Pulisci campo
            ricaricaLista(); // Aggiorna lista
            errorLabel.setText("Libreria creata con successo!");
            errorLabel.setStyle("-fx-text-fill: #27ae60;"); // Verde per successo
        }
        else
        {
            errorLabel.setText(result.getMessage());
            errorLabel.setStyle(""); // Resetta stile (usa quello del CSS per errori)
        }
    }

    /**
     * Gestisce l'eliminazione di una libreria selezionata.
     * Richiede conferma all'utente prima dell'eliminazione definitiva.
     *
     * @param event evento associato al click sul pulsante "Elimina Libreria"
     * @throws RemoteException se non riesce a comunicare con il repository remoto
     */
    @FXML
    void btnClickEliminaLibreria(ActionEvent event) throws RemoteException
    {
        if(libreria == null) return;

        if(!confermaEliminazione)
        {
            errorLabel.setText("Attenzione: Se elimini la libreria perderai i libri al suo interno. Premi di nuovo per confermare.");
            errorLabel.setStyle("-fx-text-fill: #F39C12;"); // Arancione warning
            confermaEliminazione = true;
        }
        else
        {
            DeleteLibResult result = App.getInstance().authedBookRepository.eliminaLibreria(libreria.id);
            if(result == DeleteLibResult.OK)
            {
                ricaricaLista();
                errorLabel.setText("");
                confermaEliminazione = false;
                btnEliminaLibreria.setVisible(false);
                btnGuardaLibreria.setVisible(false);
            }
            else
            {
                errorLabel.setText(result.getMessage());
            }
        }
    }

    /**
     * Apre la libreria selezionata.
     */
    @FXML
    void btnClickGuardaLibreria()
    {
        if(libreria != null) {
            App.getInstance().changeScene("Libreria.fxml");
        }
    }

    /**
     * Accede alla schermata di ricerca libri.
     *
     * @param event evento associato al click sul pulsante di ricerca
     * @throws IOException se la scena non può essere caricata
     */
    @FXML
    void btnClickRicercaLibri(ActionEvent event) throws IOException
    {
        App.getInstance().changeScene("Benvenuto.fxml");
    }

    /**
     * Gestisce il logout dell'utente.
     * Reimposta lo stato utente a ospite e ritorna alla schermata principale.
     *
     * @param event evento associato al click sul pulsante di logout
     * @throws IOException se la scena non può essere caricata
     */
    @FXML
    void btnClickLogout(ActionEvent event) throws IOException
    {
        LoginController.user = Utente.OSPITE;
        App.getInstance().authedBookRepository.logout();
        App.getInstance().changeScene("Benvenuto.fxml");
    }
}
