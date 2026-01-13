package com.lab_b.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;
import javafx.scene.control.ListView;

public class LibrerieUtenteController {

    @FXML private ListView<String> listaLibriUtente;

    @FXML
    void btnClickCerca(ActionEvent event)
    {

    }

    @FXML
    void btnClickCreaLib(ActionEvent event) throws IOException
    {
        App.getInstance().changeScene("CreaLibreria.fxml");
    }


    @FXML
    void btnClickRicercaLibrerie(ActionEvent event) throws IOException
    {
        App.getInstance().changeScene("RicercheLibrerie.fxml");
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
        App.getInstance().changeScene("Benvenuto.fxml");
    }

}
