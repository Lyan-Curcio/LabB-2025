package com.bookrecommender.client.controller;

import com.bookrecommender.common.dto.Library;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class RicercaLibrerieController {

    @FXML private ListView<String> listaLibrerie;
    @FXML private TextField ricercaLibrerie;
    @FXML private SplitMenuButton tipiDiRicerca;
    @FXML private Label errorLabel;
    @FXML private Button btnInfoLibreria;

    private HashMap<String,Library> ricercaMapLibraries = new HashMap<>();
    public static Library libreria;

    public enum TipoRicercaLibrerie
    {
        UTENTE,
        NOME_LIBRERIA
    }

    private TipoRicercaLibrerie tipoRicercaLibrerie =  TipoRicercaLibrerie.NOME_LIBRERIA;

    @FXML
    private void initialize()
    {
        btnInfoLibreria.setVisible(false);
    }
    @FXML
    void ricercaUtente(ActionEvent event)
    {
        tipoRicercaLibrerie = TipoRicercaLibrerie.UTENTE;
        tipiDiRicerca.setText("Utente");
    }
    @FXML
    void ricercaNomeLibreria(ActionEvent event)
    {
        tipoRicercaLibrerie = TipoRicercaLibrerie.NOME_LIBRERIA;
        tipiDiRicerca.setText("Libreria");
    }
    @FXML
    void btnClickCerca(ActionEvent event)
    {
        List<Library> librerie = null;
        if(ricercaLibrerie.getText().isEmpty())
        {
            errorLabel.setText("ricerca non valida");
            return;
        }
        try {
            if(tipoRicercaLibrerie == TipoRicercaLibrerie.NOME_LIBRERIA)
            {
                librerie = App.getInstance().authedBookRepository.cercaLibreriePerNome(ricercaLibrerie.getText());
            }
            else if(tipoRicercaLibrerie == TipoRicercaLibrerie.UTENTE)
            {
                librerie = App.getInstance().authedBookRepository.cercaLibreriePerUtente(ricercaLibrerie.getText());
            }
            errorLabel.setText("");
        }
        catch (RemoteException e)
        {
            listaLibrerie.getItems().clear();
            errorLabel.setText("c'è stato un errore durante la ricerca");
            e.printStackTrace();
        }
        if (librerie == null || librerie.isEmpty())
        {
            errorLabel.setText("non è stata trovata nessuna libreria");
            return;
        }

        ricercaMapLibraries.clear();
        librerie.forEach(library ->{
            ricercaMapLibraries.put(library.toStringDebug(), library);
        });

        listaLibrerie.setItems(
                FXCollections.observableArrayList(
                        librerie.stream()
                                .map(Library::toStringDebug)
                                .collect(Collectors.toList())
                )
        );

        listaLibrerie.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1)
            {
                libreria = ricercaMapLibraries.get(listaLibrerie.getSelectionModel().getSelectedItem());
                btnInfoLibreria.setVisible(true);
            }
        });
    }
    @FXML
    void btnClickInfoLibreria(ActionEvent event)
    {
        App.getInstance().changeScene("Libreria.fxml");
    }

    //bottoni di navigazione
    @FXML
    void btnClickCreaLib(ActionEvent event)
    {
        App.getInstance().changeScene("CreaLibreria.fxml");
    }

    @FXML
    void btnClickLeTueLib(ActionEvent event)
    {
        App.getInstance().changeScene("LibrerieUtente.fxml");
    }

    @FXML
    void btnClickLogout(ActionEvent event) throws RemoteException
    {
        LoginController.user = Utente.OSPITE;
        App.getInstance().authedBookRepository.logout();
        App.getInstance().changeScene("Benvenuto.fxml");
    }

    @FXML
    void btnClickRicercaLib(ActionEvent event)
    {
        App.getInstance().changeScene("Benvenuto.fxml");
    }

}

