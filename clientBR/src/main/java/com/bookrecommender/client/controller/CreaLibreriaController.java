package com.bookrecommender.client.controller;

import com.bookrecommender.common.AuthedBookRepositoryService;
import com.bookrecommender.common.BRPair;
import com.bookrecommender.common.dto.Library;
import com.bookrecommender.common.enums.library.CreateLibResult;
import com.bookrecommender.common.enums.rating.CreateRatingResult;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class CreaLibreriaController {

    @FXML private TextField tfNomeLibreria;
    @FXML private ListView<String> listaLibrerie;
    @FXML private Label errorLabel;

    @FXML private void initialize() throws RemoteException
    {
        LinkedList<Library> librerie = App.getInstance().authedBookRepository.getMyLibrerie();
        listaLibrerie.setItems(
                FXCollections.observableArrayList(
                        librerie.stream()
                        .map(Library :: toStringInfo)
                        .collect(Collectors.toList())
                )
        );
    }

    @FXML
    void creaLibreria(ActionEvent event) throws RemoteException
    {
        errorLabel.setText("");
        if(tfNomeLibreria.getText().isEmpty())
        {
            errorLabel.setText("inserire il nome della libreria");
            return;
        }

        CreateLibResult result = App.getInstance().authedBookRepository.creaLibreria(tfNomeLibreria.getText());

        if(result == CreateLibResult.OK)
        {
            // Ricarica lista
            listaLibrerie.getItems().clear();
            LinkedList<Library> librerie = App.getInstance().authedBookRepository.getMyLibrerie();
            listaLibrerie.setItems(
                FXCollections.observableArrayList(
                    librerie.stream()
                        .map(Library :: toStringInfo)
                        .collect(Collectors.toList())
                )
            );
        }
        else if(result == CreateLibResult.DUPLICATE_NAME)
        {
            errorLabel.setText(result.getMessage());
        }
        else
        {
            errorLabel.setText(result.getMessage());
        }

    }


    //bottoni di navigazione

    @FXML
    void btnClickLogout(ActionEvent event) throws IOException
    {
        LoginController.user = Utente.OSPITE;
        App.getInstance().authedBookRepository.logout();
        App.getInstance().changeScene("Benvenuto.fxml");
    }

    @FXML
    void btnClickRicercaLibri(ActionEvent event) throws IOException
    {
        App.getInstance().changeScene("Benvenuto.fxml");
    }

    @FXML
    void btnClickTueLib(ActionEvent event) throws IOException
    {
        App.getInstance().changeScene("LibrerieUtente.fxml");
    }

}


