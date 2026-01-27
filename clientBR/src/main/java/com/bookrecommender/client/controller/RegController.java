package com.bookrecommender.client.controller;

import java.rmi.RemoteException;

import com.bookrecommender.common.AuthedBookRepositoryService;
import com.bookrecommender.common.BRPair;
import com.bookrecommender.common.dto.User;
import com.bookrecommender.common.enums.auth.RegisterResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RegController {

    @FXML private TextField tfnome;
    @FXML private TextField tfcognome;
    @FXML private TextField tfUserid;
    @FXML private TextField tfCodiceFiscale;
    @FXML private TextField tfEmail;
    @FXML private TextField tfCreaPassword;
    @FXML private TextField tfConfermaPassword;
    @FXML private Label errorCF, errorEmail, errorPassword, errorUserId, errorUnexpected;

    @FXML
    void btnClick(ActionEvent event) throws RemoteException {
        String password = tfCreaPassword.getText();
        String cPassword = tfConfermaPassword.getText();

        resetErrorLabels();

        if(!password.equals(cPassword))
        {
            errorPassword.setText("le password non coincidono");
        }

        User newUser = new User(
            tfUserid.getText(),
            tfnome.getText(),
            tfcognome.getText(),
            tfCodiceFiscale.getText(),
            tfEmail.getText()
        );

        // Qui andrà il codice per salvare nel DB PostgreSQL
        // Per ora stampiamo solo a video
        System.out.println("Tentativo registrazione:\n" + newUser.toStringDebug());

        BRPair<RegisterResult, AuthedBookRepositoryService> result = App.getInstance().bookRepository.registrazione(newUser, password);

        // TODO rimuovere log
        System.out.println(result);

        if (result.first() == RegisterResult.OK)
        {
            App.getInstance().authedBookRepository = result.second();
            LoginController.user = com.bookrecommender.client.controller.Utente.REGISTRATO;
            LoginController.userId = tfUserid.getText();
            App.getInstance().changeScene("Benvenuto.fxml");
        }
        else if(result.first() == RegisterResult.DUPLICATE_EMAIL)
        {
            this.errorEmail.setText(result.first().getMessage());
        }
        else if (result.first() == RegisterResult.DUPLICATE_CF)
        {
            this.errorCF.setText(result.first().getMessage());
        }
        else if (result.first() == RegisterResult.DUPLICATE_USERID)
        {
            this.errorUserId.setText(result.first().getMessage());
        } else
        {
            this.errorUnexpected.setText(result.first().getMessage());
        }
    }

    @FXML
    void btnReturn(ActionEvent event)
    {
        App m = App.getInstance();
        m.changeScene("Benvenuto.fxml");
    }

    //
    private void resetErrorLabels()
    {
        this.errorCF.setText("");
        this.errorEmail.setText("");
        this.errorPassword.setText("");
        this.errorUserId.setText("");
        this.errorUnexpected.setText("");
    }
}