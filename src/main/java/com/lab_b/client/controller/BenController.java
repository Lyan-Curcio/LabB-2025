package com.lab_b.client.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class BenController {

    @FXML private Button ButtonResearch;

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