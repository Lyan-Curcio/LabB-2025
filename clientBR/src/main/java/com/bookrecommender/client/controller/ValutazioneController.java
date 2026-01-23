package com.bookrecommender.client.controller;

import com.bookrecommender.common.dto.BookInfo;
import com.bookrecommender.common.dto.Rating;
import com.bookrecommender.common.dto.Suggestion;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javafx.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.stream.Collectors;

public class ValutazioneController
{
    @FXML private Label infoLibro;
    @FXML private ListView<String> listaConsigliati = new ListView<>(), listaRecensioni = new ListView<>();

    private BookInfo bookInfo;
    @FXML
    private void initialize() throws RemoteException
    {
        infoLibro.setText("");

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
                                .map(Suggestion::toStringDebug)
                                .collect(Collectors.toList())
                )
        );
    }
    @FXML
    private void btnClickReturn(ActionEvent event)
    {
        App.getInstance().changeScene("Benvenuto.fxml");
    }
}
