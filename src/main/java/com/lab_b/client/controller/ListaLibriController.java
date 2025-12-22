package com.lab_b.client.controller;

import java.io.IOException;

import javax.swing.Action;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
        App m = new App();
        m.changeScene("Login.fxml");
    }

    @FXML
    void BtnClickLogout(ActionEvent event) throws IOException 
    {
        App m = new App();
        m.changeScene("Benvenuto.fxml");
    }

    @FXML
    void BtnClickRicercaLib(ActionEvent event) throws IOException 
    {
        App m = new App();
        m.changeScene("RicercheLibrerie.fxml");
    }

    @FXML
    void BtnClickTueLib(ActionEvent event) throws IOException 
    {
        App m = new App();
        m.changeScene("LibrerieUtente.fxml");
    }

    @FXML
    void BtnReaserch(ActionEvent event) 
    {

    }

}

