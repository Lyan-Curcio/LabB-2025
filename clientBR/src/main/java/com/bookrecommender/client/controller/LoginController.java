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

/**
 * Controller per gestire la schermata di login dell'applicazione.
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class LoginController {

    /** Campo di testo per inserire l'ID utente */
    @FXML private TextField UserID;

    /** Campo di testo per inserire la password */
    @FXML private PasswordField Password;

    /** Label per mostrare errori relativi all'ID utente */
    @FXML private Label errorUserid;

    /** Label per mostrare errori relativi alla password */
    @FXML private Label errorPassword;

    /** Label per mostrare errori imprevisti durante il login */
    @FXML private Label errorUnexpected;

    /** ID utente dell'utente attualmente loggato */
    public static String userId;

    /** Rappresenta lo stato dell'utente (OSPITE o REGISTRATO) */
    public static Utente user = Utente.OSPITE;

    /**
     * Gestisce il click sul pulsante di login verificando le credenziali inserite.
     *
     * @param event Evento generato dal click sul pulsante
     * @throws RemoteException Se ci sono problemi di comunicazione con il servizio remoto
     */
    @FXML
    private void BtnClickLog(ActionEvent event) throws RemoteException {
        CheckLogin();
    }

    /**
     * Cambia la scena corrente con la schermata "Benvenuto.fxml".
     *
     * @param event Evento generato dal click sul pulsante
     */
    @FXML
    private void BtnReturn(ActionEvent event) {
        App m = App.getInstance();
        m.changeScene("Benvenuto.fxml");
    }

    /**
     * Verifica le credenziali inserite dall'utente.
     * <p>
     *   Se il login ha successo, aggiorna l'utente loggato e cambia la scena.
     *   Se il login fallisce, aggiorna le label di errore appropriate.
     * </p>
     *
     * @throws RemoteException Se ci sono problemi di comunicazione con il servizio remoto
     */
    private void CheckLogin() throws RemoteException {
        userId = UserID.getText();
        String pwd = Password.getText();

        resetErrorLabels();

        BRPair<LoginResult, AuthedBookRepositoryService> result = App.getInstance().bookRepository.login(userId, pwd);

        System.out.println(result);

        if (result.first() == LoginResult.OK) {
            App.getInstance().authedBookRepository = result.second();
            user = Utente.REGISTRATO;
            App.getInstance().changeScene("Benvenuto.fxml");
        } else if (result.first() == LoginResult.USER_ID_NOT_FOUND) {
            errorUserid.setText(result.first().getMessage());
        } else if (result.first() == LoginResult.INCORRECT_PASSWORD) {
            errorPassword.setText(result.first().getMessage());
        } else {
            errorUnexpected.setText(result.first().getMessage());
        }
    }

    /**
     * Resetta tutte le label degli errori nella schermata di login.
     * <p>
     *   Viene chiamato prima di ogni tentativo di login per pulire eventuali messaggi precedenti.
     * </p>
     */
    private void resetErrorLabels() {
        this.errorUserid.setText("");
        this.errorPassword.setText("");
        this.errorUnexpected.setText("");
    }
}
