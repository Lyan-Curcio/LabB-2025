package com.lab_b.client.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class RegController {

    @FXML private TextField Tfnome;
    @FXML private TextField Tfcognome;
    @FXML private TextField TfUserid;
    @FXML private TextField TfCodiceFiscale;
    @FXML private TextField TfEmail;
    @FXML private TextField TfCreaPassword;
    @FXML private TextField TfConfermaPassword;

    @FXML
    void BtnClick(ActionEvent event) throws Exception {
        String password = TfCreaPassword.getText();
        String cPassword = TfConfermaPassword.getText();

        if(!password.equals(cPassword)) {
            System.out.println("Le password non coincidono");
            return;
        }
        
        // Qui andrà il codice per salvare nel DB PostgreSQL
        // Per ora stampiamo solo a video
        System.out.println("Tentativo registrazione: " + Tfnome.getText() + " " + Tfcognome.getText());
    }

    @FXML
    void BtnReturn(ActionEvent event) throws IOException {
        App m = new App();
        m.changeScene("Benvenuto.fxml");
    }
}