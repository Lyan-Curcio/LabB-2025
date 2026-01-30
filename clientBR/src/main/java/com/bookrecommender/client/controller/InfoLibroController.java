package com.bookrecommender.client.controller;

import com.bookrecommender.common.dto.*;
import com.bookrecommender.common.enums.library.AddBookToLibResult;
import com.bookrecommender.common.extended_dto.BookInfo;
import com.bookrecommender.common.extended_dto.SuggestionCount;
import com.bookrecommender.common.extended_dto.SuggestionWithBooks;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javafx.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class InfoLibroController
{
    @FXML private Label infoLibro, errorLabel, labelListaLibrerie, labelListRecensioni, labelRecensioni,
            labelListConsigliati, labelTopConsigliati;
    @FXML private ListView<String> listaConsigliati, listaRecensioni, listaLibrerieUtente, listTopConsigliati;
    @FXML private Button btnAggiungiLibro;

    private LinkedList<Library> librerie;
    private BookInfo bookInfo;
    private Hashtable<String, Library> librerieMap = new Hashtable<String, Library>();

    @FXML
    private void initialize() throws RemoteException
    {
        infoLibro.setText("");
        btnAggiungiLibro.setVisible(false);
        listaLibrerieUtente.setVisible(false);
        labelListaLibrerie.setVisible(false);
        if(LoginController.user == Utente.REGISTRATO)
        {
            labelListaLibrerie.setVisible(true);
            listaLibrerieUtente.setVisible(true);
            registrato();
        }
        else
        {
            ospite();
        }
    }

    private void ospite() throws RemoteException
    {
        bookInfo = App.getInstance().bookRepository.getBookInfo(BenController.libro.id);

        infoLibro.setText(bookInfo.book.toStringInfo());

        listaRecensioni.setItems(
                FXCollections.observableArrayList(
                        bookInfo.ratings.stream()
                                .map(Rating::toStringInfo)
                                .collect(Collectors.toList())
                )
        );

        listaConsigliati.setItems(
                FXCollections.observableArrayList(
                        bookInfo.suggestions.stream()
                                .map(SuggestionWithBooks::toStringInfo)
                                .collect(Collectors.toList())
                )
        );
        verificheListeRecensioniConsigliati();
    }
    private void registrato() throws RemoteException
    {
        librerie = App.getInstance().authedBookRepository.getMyLibrerie();
        bookInfo = App.getInstance().bookRepository.getBookInfo(BenController.libro.id);

        infoLibro.setText(bookInfo.book.toStringInfo());

        listaRecensioni.setItems(
                FXCollections.observableArrayList(
                        bookInfo.ratings.stream()
                                .map(Rating::toStringInfo)
                                .collect(Collectors.toList())
                )
        );
        listaConsigliati.setItems(
                FXCollections.observableArrayList(
                        bookInfo.suggestions.stream()
                                .map(SuggestionWithBooks::toStringInfo)
                                .collect(Collectors.toList())
                )
        );
        verificheListeRecensioniConsigliati();

        listaLibrerieUtente.setItems(
                FXCollections.observableArrayList(
                        librerie.stream()
                                .map(Library::toStringInfo)
                                .collect(Collectors.toList())
                )
        );

        librerie.forEach(l->{
            librerieMap.put(l.toStringInfo(), l);
        });
        listaLibrerieUtente.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1)
            {
                LibrerieUtenteController.libreria = librerieMap.get(listaLibrerieUtente.getSelectionModel().getSelectedItem());
                btnAggiungiLibro.setVisible(true);
            }
        });
    }

    private void verificheListeRecensioniConsigliati()
    {
        if(!listaRecensioni.getItems().isEmpty())
        {
            labelListRecensioni.setText("");

            labelRecensioni.setText("Stile: "+bookInfo.averageRatings.stile+"\nContenuto "+bookInfo.averageRatings.contenuto+
                    "\nGradevolezza "+bookInfo.averageRatings.gradevolezza+
                    "\nOriginalita "+bookInfo.averageRatings.originalita+
                    "\nEdizione "+bookInfo.averageRatings.edizione+
                    "\nFinale "+bookInfo.averageRatings.finale);
        }

        if(!listaConsigliati.getItems().isEmpty())
        {
            labelListConsigliati.setText("");
            labelTopConsigliati.setVisible(true);
            listTopConsigliati.setVisible(true);
            listTopConsigliati.setItems(
                    FXCollections.observableArrayList(
                            FXCollections.observableArrayList(
                                    bookInfo.suggestionCounts.stream()
                                            .map(SuggestionCount:: toStringInfo)
                                            .collect(Collectors.toList())
                            )
                    )
            );
        }
    }

    @FXML
    void btnClickAggiungiLibro(ActionEvent event) throws RemoteException
    {
        AddBookToLibResult result = App.getInstance().authedBookRepository.aggiungiLibroALibreria(LibrerieUtenteController.libreria.id, BenController.libro.id);
        if(result == AddBookToLibResult.OK)
        {
            App.getInstance().changeScene("Libreria.fxml");
        }
        else if(result == AddBookToLibResult.BOOK_ALREADY_IN_LIBRARY)
        {
            errorLabel.setText(result.getMessage());
        }
        else  if(result == AddBookToLibResult.LIBRARY_NOT_FOUND)
        {
            errorLabel.setText(result.getMessage());
        }
        else
        {
            errorLabel.setText(result.getMessage());
        }
    }
    @FXML
    private void btnClickReturn(ActionEvent event)
    {
        App.getInstance().changeScene("Benvenuto.fxml");
    }
}
