package com.bookrecommender.client.controller;

import com.bookrecommender.common.dto.Rating;
import com.bookrecommender.common.enums.rating.CreateRatingResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;

/**
 * Controller per la gestione della valutazione di un libro.
 * Permette all'utente di inserire voti e note su diversi aspetti del libro e di confermare o annullare la recensione.
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class ValutazioneController {

    /** Label per la visualizzazione degli errori di inserimento. */
    @FXML private Label errorLabel;

    /** TextArea per le note sui vari aspetti del libro. */
    @FXML private TextArea noteContenuto, noteEdizione, noteFinali, noteGradevolezza ,noteOriginalita, noteStile;

    /** TextField per i voti numerici sui vari aspetti del libro. */
    @FXML private TextField tfContenuto, tfEdizione, tfGradevolezza, tfOriginalita, tfStile;

    /** Label per visualizzare il nome dell'utente corrente. */
    @FXML private Label labelNomeUtente;

    /** Oggetto Rating che rappresenta la recensione corrente. */
    private Rating recensione;

    /**
     * Metodo per l'inizializzazione del controller.
     * Imposta il nome dell'utente o "Ospite" se non registrato.
     */
    @FXML
    private void initialize() {
        if (LoginController.user == Utente.REGISTRATO) {
            labelNomeUtente.setText("Utente: " + LoginController.userId);
        } else {
            labelNomeUtente.setText("Ospite");
        }
    }

    /**
     * Metodo chiamato al click del pulsante di conferma recensione.
     * Controlla la validità dei dati, crea l'oggetto Rating e invia la recensione.
     *
     * @param event Evento generato dal click.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     */
    @FXML
    void confermaRecensione(ActionEvent event) throws RemoteException
    {
        // Rimuove spazi vuoti iniziali e finali
        noteContenuto.setText(noteContenuto.getText().trim());
        noteEdizione.setText(noteEdizione.getText().trim());
        noteGradevolezza.setText(noteGradevolezza.getText().trim());
        noteOriginalita.setText(noteOriginalita.getText().trim());
        noteStile.setText(noteStile.getText().trim());
        noteFinali.setText(noteFinali.getText().trim());

        tfContenuto.setText(tfContenuto.getText().trim());
        tfEdizione.setText(tfEdizione.getText().trim());
        tfGradevolezza.setText(tfGradevolezza.getText().trim());
        tfOriginalita.setText(tfOriginalita.getText().trim());
        tfStile.setText(tfStile.getText().trim());

        // Controllo dei campi obbligatori
        if(!checkRecensione().equals("")) {
            errorLabel.setText(checkRecensione());
            return;
        }

        // Controllo validità dei voti e delle note
        if(!voti().equals("")) {
            errorLabel.setText(voti());
            return;
        }

        // Invio della recensione al repository
        CreateRatingResult result = App.getInstance().authedBookRepository.inserisciValutazioneLibro(recensione);
        if(result == CreateRatingResult.OK) {
            App.getInstance().changeScene("Libreria.fxml");
        } else {
            // Gestione degli altri possibili risultati
            errorLabel.setText(result.getMessage());
        }
    }

    /**
     * Controlla se tutti i campi obbligatori sono stati compilati.
     *
     * @return Stringa vuota se tutti i campi sono compilati, altrimenti un messaggio di errore.
     */
    private String checkRecensione() {
        if(tfContenuto.getText().isEmpty() || tfEdizione.getText().isEmpty() || tfGradevolezza.getText().isEmpty()
            || tfOriginalita.getText().isEmpty() || tfStile.getText().isEmpty()) {
            return "Riempire tutti i campi obbligatori(*)";
        }
        return "";
    }

    /**
     * Controlla se i voti inseriti sono numerici e nel range corretto (1-5)
     * e se le note non superano i 256 caratteri.
     *
     * @return Stringa vuota se tutti i dati sono validi, altrimenti un messaggio di errore.
     */
    private String voti() {
        int votoContenuto, votoEdizione, votoGradevolezza, votoOriginalita, votoStile;
        try {
            votoContenuto = Integer.parseInt(tfContenuto.getText());
            votoEdizione = Integer.parseInt(tfEdizione.getText());
            votoGradevolezza = Integer.parseInt(tfGradevolezza.getText());
            votoOriginalita = Integer.parseInt(tfOriginalita.getText());
            votoStile = Integer.parseInt(tfStile.getText());

            if (verificaRango(votoContenuto) && verificaRango(votoEdizione) && verificaRango(votoGradevolezza)
                && verificaRango(votoOriginalita) && verificaRango(votoStile)
                && verificaCaratteri(noteContenuto.getText().length()) &&  verificaCaratteri(noteEdizione.getText().length())
                && verificaCaratteri(noteGradevolezza.getText().length()) && verificaCaratteri(noteOriginalita.getText().length())
                && verificaCaratteri(noteStile.getText().length()) && verificaCaratteri(noteFinali.getText().length())) {
                recensione = new Rating(
                    LibreriaController.libro.id, LoginController.userId, votoStile, votoContenuto, votoGradevolezza,
                    votoOriginalita, votoEdizione, noteStile.getText(), noteContenuto.getText(),
                    noteGradevolezza.getText(), noteOriginalita.getText(), noteEdizione.getText(),
                    noteFinali.getText()
                );
                return "";
            } else {
                return "i voti devono essere tra 1 e 5 e le note sono massimo 256 caratteri";
            }
        } catch (NumberFormatException e) {
            return "i voti devono essere numerici";
        }
    }

    /**
     * Verifica se un voto numerico è nel range 1-5.
     *
     * @param voto Voto da controllare.
     * @return true se il voto è valido, false altrimenti.
     */
    private boolean verificaRango(int voto) {
        return voto >= 1 && voto <= 5;
    }

    /**
     * Controlla che il numero di caratteri di una nota non superi 256.
     *
     * @param numCaratteri Numero di caratteri della nota.
     * @return true se valido, false altrimenti.
     */
    private boolean verificaCaratteri(int numCaratteri) {
        return numCaratteri <= 256;
    }

    /**
     * Gestisce il click del pulsante per tornare indietro.
     * Torna alla scena "Libreria.fxml".
     *
     * @param event Evento generato dal click.
     */
    @FXML
    void btnReturn(ActionEvent event) {
        App.getInstance().changeScene("Libreria.fxml");
    }

    /**
     * Gestisce il click del pulsante di logout.
     * Imposta l'utente come Ospite, effettua il logout e torna alla scena di benvenuto.
     *
     * @param event Evento generato dal click.
     * @throws RemoteException Se si verifica un errore di comunicazione RMI.
     */
    @FXML
    void btnClickLogout(ActionEvent event) throws RemoteException {
        LoginController.user = Utente.OSPITE;
        App.getInstance().authedBookRepository.logout();
        App.getInstance().changeScene("Benvenuto.fxml");
    }
}
