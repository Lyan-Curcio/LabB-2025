package com.bookrecommender.client.controller;

import com.bookrecommender.common.dto.Library;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;
import java.util.List;

public class RicercaLibrerieController {

    @FXML
    private ListView<?> listaLibrerie;
    @FXML
    private TextField ricercaLibrerie;
    @FXML
    private SplitMenuButton tipiDiRicerca;

    public enum TipoRicercaLibrerie
    {
        UTENTE,
        NOME_LIBRERIA
    }

    private TipoRicercaLibrerie tipoRicercaLibrerie =  TipoRicercaLibrerie.NOME_LIBRERIA;

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

        }
        try {
            if(tipoRicercaLibrerie == TipoRicercaLibrerie.NOME_LIBRERIA)
            {
                App.getInstance().authedBookRepository.cercaLibreriePerNome(ricercaLibrerie.getText());
            }
            else if(tipoRicercaLibrerie == TipoRicercaLibrerie.UTENTE)
            {
                App.getInstance().authedBookRepository.cercaLibreriePerUtente(ricercaLibrerie.getText());
            }
        } catch (RemoteException e)
        {
            throw new RuntimeException(e);
        }

    }

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
    void btnClickLogout(ActionEvent event)
    {
        LoginController.user = Utente.OSPITE;
        App.getInstance().changeScene("Benvenuto.fxml");
    }

    @FXML
    void btnClickRicercaLib(ActionEvent event)
    {
        App.getInstance().changeScene("Benvenuto.fxml");
    }

}

