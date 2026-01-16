package com.bookrecommender.client.controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.SplitMenuButton;

public class ListaLibriController {

    @FXML
    private ListView<?> myListView;

    @FXML
    private TextField TfRicerca;
    
    @FXML
    private Label InfoBook;

    @FXML
    private SplitMenuButton TipiDiRicerca;

    private String scelta;
    
    //sezione per la selezione del tipo di ricerca
    @FXML
    void RicercaAutore(ActionEvent event)
    {
        scelta="Autore";
        TipiDiRicerca.setText("Autore");
    }
    @FXML
    void RicercaTitolo(ActionEvent event)
    {
        scelta="Titolo";
        TipiDiRicerca.setText("Titolo");
    }
    @FXML
    void RicercaAnno(ActionEvent event)
    {
        scelta="Anno";
        TipiDiRicerca.setText("Anno");
    }


    //bottoni di navigazione e ricerca
    @FXML
    void BtnClickCreaLib(ActionEvent event) throws IOException 
    {
        App m = App.getInstance();
        m.changeScene("CreaLibreria.fxml");
    }

    @FXML
    void BtnClickLogout(ActionEvent event)
    {
        App m = App.getInstance();
        m.changeScene("Benvenuto.fxml");
    }

    @FXML
    void BtnClickRicercaLib(ActionEvent event)
    {
        App m = App.getInstance();
        m.changeScene("RicercheLibrerie.fxml");
    }

    @FXML
    void BtnClickTueLib(ActionEvent event)
    {
        App m = App.getInstance();
        m.changeScene("LibrerieUtente.fxml");
    }

    @FXML
    void BtnReaserch(ActionEvent event) 
    {

    }
}

