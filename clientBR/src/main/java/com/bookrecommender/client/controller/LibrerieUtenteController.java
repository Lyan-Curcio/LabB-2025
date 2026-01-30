package com.bookrecommender.client.controller;

import com.bookrecommender.common.dto.Library;
import com.bookrecommender.common.enums.library.DeleteLibResult;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.stream.Collectors;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class LibrerieUtenteController {

    @FXML private ListView<String> listaLibrerieUtente;
    @FXML private Label listLabel, errorLabel;
    @FXML private TextField tfCercaLibrerie;
    @FXML private Button btnEliminaLibreria, btnGuardaLibreria;

    private LinkedList<Library> librerie;
    private Hashtable<String, Library> mapLibrerie =  new Hashtable<>();
    private boolean confermaEliminazione = false;

    public static Library libreria;
    @FXML
    private void initialize() throws RemoteException
    {

        librerie = App.getInstance().authedBookRepository.getMyLibrerie();
        if(librerie.isEmpty())
        {
            listLabel.setText("Nessuna libreria");
        }
        librerie.forEach(l->{
            mapLibrerie.put(l.toStringInfo(), l);
        });

        listaLibrerieUtente.setItems(
                FXCollections.observableArrayList(
                        librerie.stream()
                                .map(Library :: toStringInfo)
                                .collect(Collectors.toList())
                )
        );
        listaLibrerieUtente.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1)
            {
                libreria = mapLibrerie.get(listaLibrerieUtente.getSelectionModel().getSelectedItem());
                confermaEliminazione = false;
                errorLabel.setText("");
                btnEliminaLibreria.setVisible(true);
                btnGuardaLibreria.setVisible(true);
            }
        });

    }
    @FXML
    void btnClickEliminaLibreria(ActionEvent event) throws RemoteException
    {
        if(!confermaEliminazione)
        {
            errorLabel.setText("nella libreria possono esserci dei libri valutati e consigliati, e questi possono non essere in altre librerie!");
            confermaEliminazione = true;
        }
        else
        {
            DeleteLibResult result = App.getInstance().authedBookRepository.eliminaLibreria(libreria.id);
            if(result == DeleteLibResult.OK)
            {
                App.getInstance().changeScene("LibrerieUtente.fxml");
            }
            else if(result == DeleteLibResult.LIBRARY_NOT_FOUND)
            {
                errorLabel.setText(result.getMessage());
            }
            else
            {
                errorLabel.setText(result.getMessage());
            }
        }
    }
    @FXML
    void btnClickGuardaLibreria()
    {
        App.getInstance().changeScene("Libreria.fxml");
    }

    @FXML
    void btnClickCreaLib(ActionEvent event) throws IOException
    {
        App.getInstance().changeScene("CreaLibreria.fxml");
    }
    @FXML
    void btnClickRicercaLibri(ActionEvent event) throws IOException
    {
        App.getInstance().changeScene("Benvenuto.fxml");
    }
    @FXML
    void btnClickLogout(ActionEvent event) throws IOException
    {
        LoginController.user = Utente.OSPITE;
        App.getInstance().authedBookRepository.logout();
        App.getInstance().changeScene("Benvenuto.fxml");
    }

}
