package com.bookrecommender.client.controller;

import com.bookrecommender.common.dto.Book;
import com.bookrecommender.common.dto.Rating;
import com.bookrecommender.common.dto.Suggestion;
import com.bookrecommender.common.enums.library.RemoveBookFromLibResult;
import com.bookrecommender.common.enums.suggestion.AddSuggestionResult;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.stream.Collectors;


public class LibreriaController
{
    @FXML private Button btnConsiglia, btnValuta;
    @FXML private Button btnRimuoviConsilgio, btnRimuoviLibro, btnRimuoviRecensione;
    @FXML private ListView<String> libriConsigliati, listaLibriLibreria;
    @FXML private Label nomeLibreria, errorLabel, recensione;

    private Hashtable<String, Book> ricercaMapLibri =  new Hashtable<>();
    private Hashtable<String, Suggestion> MapSuggerimenti = new Hashtable<>();

    private Book libro;
    public static Suggestion consiglio = null;
    private Rating valutazione = null;

    @FXML
    private void initialize() throws RemoteException
    {
        LinkedList<Book> libri;
        nomeLibreria.setText(LibrerieUtenteController.libreria.name);
        libri = App.getInstance().authedBookRepository.getLibriFromLibreria(LibrerieUtenteController.libreria.id);

        ricercaMapLibri.clear();

        libri.forEach(l->{
            ricercaMapLibri.put(l.toStringInfo(), l);
        });
        listaLibriLibreria.setItems(
                FXCollections.observableArrayList(
                        libri.stream()
                                .map(Book::toStringInfo)
                                .collect(Collectors.toList())
                )
        );

        listaLibriLibreria.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1)
            {
                libro  = ricercaMapLibri.get(listaLibriLibreria.getSelectionModel().getSelectedItem());
                LinkedList<Suggestion> consigliati;
                btnRimuoviLibro.setVisible(true);
                try
                {
                    btnConsiglia.setVisible(true);
                    consigliati = App.getInstance().authedBookRepository.getMySuggerimenti(libro.id);
                }
                catch(RemoteException e)
                {
                    libriConsigliati.getItems().clear();
                    errorLabel.setText("c'è stato un errore durante la consulta dei suggerimenti");
                    e.printStackTrace();
                    return;
                }
                consigliati.forEach(sug->{
                    MapSuggerimenti.put(sug.toString(), sug);
                });
                libriConsigliati.setItems(
                        FXCollections.observableArrayList(
                                consigliati.stream()
                                        .map(Suggestion::toStringDebug)
                                        .collect(Collectors.toList())
                        )
                );
                libriConsigliati.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String s, String t1)
                    {
                        consiglio = MapSuggerimenti.get(libriConsigliati.getSelectionModel().getSelectedItem());
                        btnRimuoviConsilgio.setVisible(true);
                    }
                });
                try
                {
                    valutazione = App.getInstance().authedBookRepository.getMyValutazione(libro.id);
                    if(valutazione == null || valutazione.toStringInfo().equals(""))
                    {
                        btnValuta.setVisible(true);
                    }
                    else
                    {
                        recensione.setText(valutazione.toStringInfo());
                        btnRimuoviRecensione.setVisible(true);
                    }
                }
                catch (RemoteException e)
                {
                    errorLabel.setText("c'è stato un errore durante l'acquisizione della recensione");
                }
            }
        });

    }

    @FXML
    void aggiungiConsigliato(ActionEvent event)
    {

    }
    @FXML
    void aggiungiValutazione(ActionEvent event)
    {

    }
    @FXML
    void rimuoviConsiglio(ActionEvent event)
    {

    }
    @FXML
    void rimuoviLibro(ActionEvent event) throws RemoteException
    {
        RemoveBookFromLibResult result = App.getInstance().authedBookRepository.rimuoviLibroDaLibreria(LibrerieUtenteController.libreria.id, libro.id);
        if (result == RemoveBookFromLibResult.OK)
        {
            App.getInstance().changeScene("Libreria.fxml");
        }
        else if(result == RemoveBookFromLibResult.BOOK_NOT_IN_LIBRARY)
        {
            errorLabel.setText(result.getMessage());
        }
        else if(result == RemoveBookFromLibResult.LIBRARY_NOT_FOUND)
        {
            errorLabel.setText(result.getMessage());
        }
        else
        {
            errorLabel.setText(result.getMessage());
        }
    }
    @FXML
    void rimuoviValutazione(ActionEvent event)
    {

    }

    @FXML
    void btnClickCreaLib(ActionEvent event)
    {
        App.getInstance().changeScene("Crealibreria.fxml");
    }
    @FXML
    void btnClickLeTueLib(ActionEvent event)
    {
        App.getInstance().changeScene("LibrerieUtente.fxml");
    }
    @FXML
    void btnClickLogout(ActionEvent event) throws RemoteException
    {
        LoginController.user = Utente.OSPITE;
        App.getInstance().authedBookRepository.logout();
        App.getInstance().changeScene("Benvenuto.fxml");
    }
    @FXML
    void btnClickRicercaLibrerie(ActionEvent event)
    {
        App.getInstance().changeScene("RicercheLibrerie.fxml");
    }
    @FXML
    void btnClickRicercaLibri(ActionEvent event)
    {
        App.getInstance().changeScene("Benvenuto.fxml");
    }
}
