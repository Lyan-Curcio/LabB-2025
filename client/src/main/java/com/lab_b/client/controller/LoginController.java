package com.lab_b.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;

public class LoginController {

    @FXML private TextField UserID;
    @FXML private PasswordField Password;
    @FXML private Button BLogin;
    @FXML private Button ButtonReturn;
    public static Utente user = Utente.OSPITE;
    @FXML
    private void BtnclickLog(ActionEvent event) throws IOException {
        CheckLogin();   
    }

    @FXML
    private void BtnReturn(ActionEvent event) throws IOException {
        App m = App.getInstance();
        m.changeScene("Benvenuto.fxml");
    }
    
    private void CheckLogin() throws IOException {
        String uid = UserID.getText();
        String pwd = Password.getText();
        App m = App.getInstance();

        // Simulazione login (da sostituire con DB in futuro)
        if("Sergio".equals(uid) && "123".equals(pwd)) {
            System.out.println("Successo");
            user = Utente.REGISTRATO;
            m.changeScene("Benvenuto.fxml"); // Decommentare quando esiste Libri.fxml
        } else if (uid.isEmpty() || pwd.isEmpty()) {
            System.out.println("Per favore inserire i dati");
        } else {
            System.out.println("Nome utente o password errati");
        }
    }
}