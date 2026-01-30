package com.bookrecommender.client.controller;

import java.rmi.RemoteException;
import java.util.regex.Pattern;

import com.bookrecommender.common.AuthedBookRepositoryService;
import com.bookrecommender.common.BRPair;
import com.bookrecommender.common.dto.User;
import com.bookrecommender.common.enums.auth.RegisterResult;
import org.apache.commons.validator.routines.EmailValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controller per la schermata di registrazione dell'applicazione BookRecommender.
 * Gestisce la validazione dei dati.
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class RegController {

    /** Campo di testo per inserire il nome dell'utente. */
    @FXML private TextField tfnome;

    /** Campo di testo per inserire il cognome dell'utente. */
    @FXML private TextField tfcognome;

    /** Campo di testo per inserire l'ID utente desiderato. */
    @FXML private TextField tfUserid;

    /** Campo di testo per inserire il codice fiscale dell'utente. */
    @FXML private TextField tfCodiceFiscale;

    /** Campo di testo per inserire l'email dell'utente. */
    @FXML private TextField tfEmail;

    /** Campo di testo per creare la password dell'utente. */
    @FXML private TextField tfCreaPassword;

    /** Campo di testo per confermare la password dell'utente. */
    @FXML private TextField tfConfermaPassword;

    /** Label per visualizzare errori relativi al codice fiscale. */
    @FXML private Label errorCF;

    /** Label per visualizzare errori relativi all'email. */
    @FXML private Label errorEmail;

    /** Label per visualizzare errori relativi alla password. */
    @FXML private Label errorPassword;

    /** Label per visualizzare errori relativi all'ID utente. */
    @FXML private Label errorUserId;

    /** Label per visualizzare errori inattesi o generici. */
    @FXML private Label errorUnexpected;

    /**
     * Gestisce il click sul pulsante di registrazione.
     * Verifica la validità dei dati inseriti, segnala eventuali errori
     * e tenta di registrare l'utente tramite il servizio remoto.
     *
     * @param event l'evento di azione generato dal click sul pulsante
     * @throws RemoteException se si verifica un errore di comunicazione con il servizio remoto
     */
    @FXML
    void btnClick(ActionEvent event) throws RemoteException {
        String password = tfCreaPassword.getText();
        String cPassword = tfConfermaPassword.getText();

        resetErrorLabels();

        // Controllo che tutti i campi siano compilati
        if (tfnome.getText().isEmpty() || tfcognome.getText().isEmpty() || tfUserid.getText().isEmpty()
            || tfCodiceFiscale.getText().isEmpty() || tfEmail.getText().isEmpty())
        {
            errorUnexpected.setText("riempire tutti i campi");
            return;
        }

        // Controllo validità email
        if (!checkEmail().equals(""))
        {
            errorEmail.setText(checkEmail());
            return;
        }

        // Controllo validità codice fiscale
        if (!checkCF().equals(""))
        {
            errorCF.setText(checkCF());
            return;
        }

        // Controllo corrispondenza password
        if(!password.equals(cPassword))
        {
            errorPassword.setText("le password non coincidono");
            return;
        }

        // Creazione nuovo utente
        User newUser = new User(
            tfUserid.getText(),
            tfnome.getText(),
            tfcognome.getText(),
            tfCodiceFiscale.getText(),
            tfEmail.getText()
        );

        // Log temporaneo per debug
        System.out.println("Tentativo registrazione:\n" + newUser.toStringDebug());

        BRPair<RegisterResult, AuthedBookRepositoryService> result = App.getInstance().bookRepository.registrazione(newUser, password);

        // Log temporaneo del risultato
        System.out.println(result);

        // Gestione risultato della registrazione
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

    /**
     * Verifica che l'email inserita sia valida.
     *
     * @return una stringa vuota se l'email è valida, altrimenti un messaggio di errore
     */
    private String checkEmail()
    {
        if (EmailValidator.getInstance().isValid(tfEmail.getText()))
        {
            return "";
        }
        return "l'email non è valida";
    }

    /**
     * Verifica che il codice fiscale inserito sia valido.
     *
     * @return una stringa vuota se il codice fiscale è valido, altrimenti un messaggio di errore
     */
    private String checkCF()
    {
        if (Pattern.matches("^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$", tfCodiceFiscale.getText()))
        {
            return "";
        }
        return "codice fiscale non valido";
    }

    /**
     * Gestisce il click sul pulsante per tornare indietro.
     * Reindirizza l'utente alla scena principale "Benvenuto.fxml".
     *
     * @param event l'evento di azione generato dal click sul pulsante
     */
    @FXML
    void btnReturn(ActionEvent event)
    {
        App m = App.getInstance();
        m.changeScene("Benvenuto.fxml");
    }

    /**
     * Resetta tutte le label di errore nella schermata di registrazione.
     */
    private void resetErrorLabels()
    {
        this.errorCF.setText("");
        this.errorEmail.setText("");
        this.errorPassword.setText("");
        this.errorUserId.setText("");
        this.errorUnexpected.setText("");
    }
}
