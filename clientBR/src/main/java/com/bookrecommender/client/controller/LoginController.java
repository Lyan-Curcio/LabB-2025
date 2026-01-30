package com.bookrecommender.client.controller;

import com.bookrecommender.common.AuthedBookRepositoryService;
import com.bookrecommender.common.BRPair;
import com.bookrecommender.common.enums.auth.LoginResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;

public class LoginController {

    @FXML private TextField UserID;
    @FXML private PasswordField Password;
    @FXML private Label errorUserid, errorPassword, errorUnexpected;

    public static String userId;
    public static Utente user = Utente.OSPITE;
    @FXML
    private void BtnClickLog(ActionEvent event) throws RemoteException
    {
        CheckLogin();   
    }

    @FXML
    private void BtnReturn(ActionEvent event)
    {
        App m = App.getInstance();
        m.changeScene("Benvenuto.fxml");
    }
    
    private void CheckLogin() throws RemoteException
    {
        userId = UserID.getText();
        String pwd = Password.getText();

        resetErrorLabels();

        BRPair<LoginResult, AuthedBookRepositoryService> result = App.getInstance().bookRepository.login(userId, pwd);

        System.out.println(result);
        // Simulazione login (da sostituire con DB in futuro)
        if(result.first() == LoginResult.OK)
        {
            App.getInstance().authedBookRepository = result.second();
            user = Utente.REGISTRATO;
            App.getInstance().changeScene("Benvenuto.fxml");
        }
        else if(result.first() == LoginResult.USER_ID_NOT_FOUND)
        {
            errorUserid.setText(result.first().getMessage());
        }
        else if(result.first() == LoginResult.INCORRECT_PASSWORD)
        {
            errorPassword.setText(result.first().getMessage());
        }
        else
        {
            errorUnexpected.setText(result.first().getMessage());
        }
    }
    private void resetErrorLabels()
    {
        this.errorUserid.setText("");
        this.errorPassword.setText("");
        this.errorUnexpected.setText("");
    }
}