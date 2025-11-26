package com.lab_b;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class ListaLibricom {

    @FXML
    private ListView<?> myListView;

    @FXML
    private TextField TfRicerca;

    @FXML
    private Button ButtonResearch;

    @FXML
    private Label InfoBook;

    @FXML
    private Button BtnCreaLib;

    @FXML
    private Button BtnRicercaLib;

    @FXML
    private Button BtnTueLib;

    @FXML
    private Button BtnLogout;

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
        m.changeScene("Benvenu.fxml");
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

