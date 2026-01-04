package com.lab_b.client.controller;

import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import com.lab_b.common.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;

public class BenController {

    @FXML 
    private Button ButtonResearch;
    @FXML
    private TextField TfRicerca;
    @FXML
    private TextField TfRicercaAnno;
    @FXML
    private SplitMenuButton TipiDiRicerca;
    @FXML
    private Label ListLabel;

    private enum TipoDiRicerca {
        TITOLO,
        AUTORE,
        AUTORE_E_ANNO
    }
    private TipoDiRicerca tipoDiRicerca = TipoDiRicerca.TITOLO;

    //sezione per la selezione del tipo di ricerca
    @FXML
    void RicercaAutore(ActionEvent event)
    {
        tipoDiRicerca = TipoDiRicerca.AUTORE;
        TipiDiRicerca.setText("Autore");
        TfRicercaAnno.setDisable(true);
    }
    @FXML
    void RicercaTitolo(ActionEvent event)
    {
        tipoDiRicerca = TipoDiRicerca.TITOLO;
        TipiDiRicerca.setText("Titolo");
        TfRicercaAnno.setDisable(true);
    }
    @FXML
    void RicercaAutoreEAnno(ActionEvent event)
    {
        tipoDiRicerca = TipoDiRicerca.AUTORE_E_ANNO;
        TipiDiRicerca.setText("Autore e anno");
        TfRicercaAnno.setDisable(false);
    }


    //bottoni di navigazione
    @FXML
    void Accedi(ActionEvent event) throws IOException
    {
        App m = App.getInstance();
        m.changeScene("Login.fxml");
    }
    
    @FXML
    void Registrati(ActionEvent event) throws IOException
    {
        App m = App.getInstance();
        m.changeScene("Registrazione.fxml");
    }

    @FXML
    void BtnResearch(ActionEvent event)
    {
        List<Book> libri = null;

        try
        {
            if (tipoDiRicerca == TipoDiRicerca.TITOLO)
            {
                libri = App.getInstance().bookRepository.cercaLibroPerTitolo(TfRicerca.getText());
            }
            else if (tipoDiRicerca == TipoDiRicerca.AUTORE)
            {
                libri = App.getInstance().bookRepository.cercaLibroPerAutore(TfRicerca.getText());
            }
            else if (tipoDiRicerca == TipoDiRicerca.AUTORE_E_ANNO)
            {
                int anno = 0;
                try
                {
                    anno = Integer.parseInt(TfRicercaAnno.getText()); // TODO gestire eccezioni
                }
                catch (NumberFormatException e)
                {
                    ListLabel.setText("L'anno cercato non è valido!");
                    return;
                }
                if (anno < 0 || anno > LocalDate.now().getYear())
                {
                    ListLabel.setText("L'anno cercato non è valido!");
                    return;
                }

                libri = App.getInstance().bookRepository.cercaLibroPerAutoreEAnno(TfRicerca.getText(), anno);
            }
        }
        catch (RemoteException e)
        {
            ListLabel.setText("C'è stato un errore durante la ricerca!");
            return;
        }

        if (libri != null && libri.isEmpty())
        {
            ListLabel.setText("Nessun libro trovato");
        }
        else
        {
            // Pulisci il messaggio nel caso fosse "sporco"
            ListLabel.setText("");
        }
    }
}