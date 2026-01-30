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
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class LibrerieUtenteController {

    @FXML private ListView<String> listaLibrerieUtente;
    @FXML private Label listLabel, errorLabel, labelNomeUtente;
    @FXML private TextField tfNomeNuovaLibreria; // Campo per creare
    @FXML private Button btnEliminaLibreria, btnGuardaLibreria;

    private LinkedList<Library> librerie;
    private Hashtable<String, Library> mapLibrerie =  new Hashtable<>();
    private boolean confermaEliminazione = false;

    public static Library libreria;

    @FXML
    private void initialize() throws RemoteException
    {
        // Imposta nome utente sidebar
        if (LoginController.user == Utente.REGISTRATO) {
            labelNomeUtente.setText("Utente: " + LoginController.userId);
        } else {
            labelNomeUtente.setText("Ospite");
        }

        // Imposta listener ridimensionamento testo liste
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

        // Listener selezione
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

    // --- NUOVO METODO: CREA LIBRERIA ---
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

    @FXML
    void btnClickGuardaLibreria()
    {
        if(libreria != null) {
            App.getInstance().changeScene("Libreria.fxml");
        }
    }

    @FXML
    void btnClickRicercaLibri(ActionEvent event) throws IOException
    {
        App.getInstance().changeScene("Benvenuto.fxml");
    }

    @FXML
    void btnClickLogout(ActionEvent event) throws IOException
    {
        LoginController.user = Utente.OSPITE;
        App.getInstance().authedBookRepository.logout();
        App.getInstance().changeScene("Benvenuto.fxml");
    }
}