package com.bookrecommender.client.controller;

import com.bookrecommender.common.dto.Rating;
import com.bookrecommender.common.enums.rating.CreateRatingResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;

public class ValutazioneController {

    @FXML private Label errorLabel;
    @FXML private TextArea noteContenuto, noteEdizione, noteFinali, noteGradevolezza ,noteOriginalita, noteStile;
    @FXML private TextField tfContenuto, tfEdizione, tfGradevolezza, tfOriginalita, tfStile;

    private Rating recensione;
    @FXML
    void confermaRecensione(ActionEvent event) throws RemoteException
    {
        noteContenuto.setText(noteContenuto.getText().trim()); noteEdizione.setText(noteEdizione.getText().trim());
        noteGradevolezza.setText(noteGradevolezza.getText().trim());  noteOriginalita.setText(noteOriginalita.getText().trim());
        noteStile.setText(noteStile.getText().trim()); noteFinali.setText(noteFinali.getText().trim());

        tfContenuto.setText(tfContenuto.getText().trim()); tfEdizione.setText(tfEdizione.getText().trim());
        tfGradevolezza.setText(tfGradevolezza.getText().trim());  tfOriginalita.setText(tfOriginalita.getText().trim());
        tfStile.setText(tfStile.getText().trim());

        if(!checkRecensione().equals(""))
        {
            errorLabel.setText(checkRecensione());
            return;
        }
        if(!voti().equals(""))
        {
            errorLabel.setText(voti());
        }

        CreateRatingResult result = App.getInstance().authedBookRepository.inserisciValutazioneLibro(recensione);
        if(result == CreateRatingResult.OK)
        {
            App.getInstance().changeScene("Libreria.fxml");
        }
        else if(result == CreateRatingResult.ALREADY_RATED)
        {
            errorLabel.setText(result.getMessage());
        }
        else if(result == CreateRatingResult.BOOK_NOT_IN_LIBRARY)
        {
            errorLabel.setText(result.getMessage());
        }
        else
        {
            errorLabel.setText(result.getMessage());
        }
    }
    private String checkRecensione()
    {
        if(
                tfContenuto.getText().isEmpty() || tfEdizione.getText().isEmpty() || tfGradevolezza.getText().isEmpty()
                || tfOriginalita.getText().isEmpty() || tfStile.getText().isEmpty()
        )
        {
            return  "Riempire tutti i campi obbligatori(*)";
        }
        return "";
    }
    private String voti()
    {
        int votoContenuto, votoEdizione, votoGradevolezza, votoOriginalita, votoStile;
        try
        {
            votoContenuto = Integer.parseInt(tfContenuto.getText());
            votoEdizione = Integer.parseInt(tfEdizione.getText());
            votoGradevolezza = Integer.parseInt(tfGradevolezza.getText());
            votoOriginalita = Integer.parseInt(tfOriginalita.getText());
            votoStile = Integer.parseInt(tfStile.getText());

            if (verificaRango(votoContenuto) && verificaRango(votoEdizione) && verificaRango(votoGradevolezza)
                    && verificaRango(votoOriginalita) && verificaRango(votoStile)
                    && verificaCaratteri(noteContenuto.getText().length()) &&  verificaCaratteri(noteEdizione.getText().length())
                    && verificaCaratteri(noteGradevolezza.getText().length()) && verificaCaratteri(noteOriginalita.getText().length())
                    && verificaCaratteri(noteStile.getText().length()) && verificaCaratteri(noteFinali.getText().length())
            )
            {
                recensione = new Rating(
                        LibreriaController.libro.id, LoginController.userId, votoStile, votoContenuto, votoGradevolezza,
                        votoOriginalita, votoEdizione, noteStile.getText(), noteContenuto.getText(),
                        noteGradevolezza.getText(), noteOriginalita.getText(), noteEdizione.getText(),
                        noteFinali.getText()
                    );
                return "";
            }
            else
            {
                return "i voti devono essere tra 1 e 5 e le note sono massimo 256 caratteri";
            }
        }
        catch (NumberFormatException e)
        {
            return "i voti devono essere numerici";
        }
    }
    private boolean verificaRango(int voto)
    {
        return voto >= 0 && voto <= 5;
    }
    private boolean verificaCaratteri(int numCaratteri)
    {
        return numCaratteri <= 256;
    }

    @FXML
    void btnReturn(ActionEvent event)
    {
        App.getInstance().changeScene("Libreria.fxml");
    }
}
