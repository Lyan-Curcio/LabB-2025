package com.bookrecommender.client.controller;

import java.rmi.RemoteException;

import com.bookrecommender.common.dto.UtentiRegistrati;
import com.bookrecommender.common.enums.auth.RegisterResult;
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
    void BtnClick(ActionEvent event) throws RemoteException {
        String password = TfCreaPassword.getText();
        String cPassword = TfConfermaPassword.getText();

        if(!password.equals(cPassword)) {
            System.out.println("Le password non coincidono");
            return;
        }

        UtentiRegistrati newUser = new UtentiRegistrati(
            TfUserid.getText(),
            Tfnome.getText(),
            Tfcognome.getText(),
            TfCodiceFiscale.getText(),
            TfEmail.getText()
        );

        // Qui andrà il codice per salvare nel DB PostgreSQL
        // Per ora stampiamo solo a video
        System.out.println("Tentativo registrazione:\n" + newUser.toStringDebug());

        RegisterResult result = App.getInstance().bookRepository.registrazione(newUser, password);

        // TODO rimuovere log
        System.out.println(result);

        if (result == RegisterResult.OK)
        {
            // TODO pulisci errore e porta a schermata nuova
        }
        else
        {
            // TODO Segnala errore
        }
    }

    @FXML
    void BtnReturn(ActionEvent event) {
        App m = App.getInstance();
        m.changeScene("Benvenuto.fxml");
    }
}