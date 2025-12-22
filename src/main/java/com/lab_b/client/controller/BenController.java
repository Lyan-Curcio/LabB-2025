package com.lab_b.client.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;

public class BenController {

    @FXML 
    private Button ButtonResearch;

    @FXML
    private SplitMenuButton TipiDiRicerca;

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


    //bottoni di navigazione
    @FXML
    void Accedi(ActionEvent event) throws IOException {
        App m = new App();
        m.changeScene("Login.fxml");
    }
    
    @FXML
    void Registrati(ActionEvent event) throws IOException {
        App m = new App();
        m.changeScene("Registrazione.fxml");
    }

    @FXML
    void BtnReaserch(ActionEvent event) {
        System.out.println("Ricerca non ancora implementata");
    }
}