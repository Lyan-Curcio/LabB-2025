package com.bookrecommender.client.controller;

import com.bookrecommender.common.dto.Book;
import com.bookrecommender.common.dto.Rating;
import com.bookrecommender.common.dto.Suggestion;
import com.bookrecommender.common.dto.SuggestionWithBooks;
import com.bookrecommender.common.enums.library.RemoveBookFromLibResult;
import com.bookrecommender.common.enums.rating.DeleteRatingResult;
import com.bookrecommender.common.enums.suggestion.AddSuggestionResult;
import com.bookrecommender.common.enums.suggestion.RemoveSuggestionResult;
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
    private Hashtable<String, SuggestionWithBooks> MapSuggerimenti = new Hashtable<>();
    private Rating valutazione = null;
    private Book libroConsigliato;

    public static Book libro;
    public static SuggestionWithBooks consiglio;
    public static boolean consigliando = false;
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
                LinkedList<SuggestionWithBooks> consigliati;
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

                MapSuggerimenti.clear();
                consigliati.forEach(sug->{
                    MapSuggerimenti.put(sug.toStringInfo(), sug);
                });
                libriConsigliati.setItems(
                        FXCollections.observableArrayList(
                                consigliati.stream()
                                        .map(SuggestionWithBooks::toStringInfo)
                                        .collect(Collectors.toList())
                        )
                );
                try
                {
                    valutazione = App.getInstance().authedBookRepository.getMyValutazione(libro.id);
                    if(valutazione == null || valutazione.toStringInfo().equals(""))
                    {
                        btnRimuoviRecensione.setVisible(false);
                        btnValuta.setVisible(true);
                        recensione.setText("");
                    }
                    else
                    {
                        btnValuta.setVisible(false);
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
        libriConsigliati.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1)
            {
                try
                {
                    consiglio = MapSuggerimenti.get(libriConsigliati.getSelectionModel().getSelectedItem());
                    libroConsigliato = consiglio.suggestedBook;
                    btnRimuoviConsilgio.setVisible(true);
                }catch (NullPointerException e)
                {
                    btnRimuoviConsilgio.setVisible(false);
                }
            }
        });

    }

    @FXML
    void aggiungiConsigliato(ActionEvent event)
    {
        consigliando = true;
        App.getInstance().changeScene("Benvenuto.fxml");
    }
    @FXML
    void aggiungiValutazione(ActionEvent event)
    {
        App.getInstance().changeScene("Valutazione.fxml");
    }
    @FXML
    void rimuoviConsiglio(ActionEvent event) throws RemoteException
    {
        RemoveSuggestionResult result = App.getInstance().authedBookRepository.rimuoviSuggerimentoLibro(libro.id, libroConsigliato.id);
        if (result == RemoveSuggestionResult.OK)
        {
            App.getInstance().changeScene("Libreria.fxml");
        }
        else if(result == RemoveSuggestionResult.NOT_SUGGESTED)
        {
            errorLabel.setText(result.getMessage());
        }
        else
        {
            errorLabel.setText(result.getMessage());
        }
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
        else if(consiglio == null)
        {
            errorLabel.setText("il libro è consigliato, rimuovilo dai consigliati e puoi eliminare il libro");
        }
        else
        {
            errorLabel.setText(result.getMessage());
        }
    }
    @FXML
    void rimuoviValutazione(ActionEvent event) throws RemoteException
    {
        DeleteRatingResult result = App.getInstance().authedBookRepository.rimuoviValutazioneLibro(libro.id);
        if(result == DeleteRatingResult.OK)
        {
            App.getInstance().changeScene("Libreria.fxml");
        }
        else if(result == DeleteRatingResult.NOT_RATED)
        {
            errorLabel.setText(result.getMessage());
        }
        else
        {
            errorLabel.setText(result.getMessage());
        }
    }

    @FXML
    void btnClickCreaLib(ActionEvent event)
    {
        App.getInstance().changeScene("CreaLibreria.fxml");
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
    void btnClickRicercaLibri(ActionEvent event)
    {
        App.getInstance().changeScene("Benvenuto.fxml");
    }
}
