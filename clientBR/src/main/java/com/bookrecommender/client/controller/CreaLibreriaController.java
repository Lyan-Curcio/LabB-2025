package com.bookrecommender.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class CreaLibreriaController {

    @FXML
    private Button btnLeTueLib;

    @FXML
    private Button btnRicercaLibreria;

    @FXML
    private Button btnRicercaLib;

    @FXML
    private Button btnLogout;

    @FXML
    void btnClickLogout(ActionEvent event) throws IOException
    {
        LoginController.user = Utente.OSPITE;
        App.getInstance().changeScene("Benvenuto.fxml");
    }

    @FXML
    void btnClickRicercaLib(ActionEvent event) throws IOException
    {
        App.getInstance().changeScene("Benvenuto.fxml");
    }

    @FXML
    void btnClickRicercaLibreria(ActionEvent event) throws IOException
    {
        App.getInstance().changeScene("RicercheLibrerie.fxml");
    }

    @FXML
    void btnClickTueLib(ActionEvent event) throws IOException
    {
        App.getInstance().changeScene("LibrerieUtente.fxml");
    }

}


