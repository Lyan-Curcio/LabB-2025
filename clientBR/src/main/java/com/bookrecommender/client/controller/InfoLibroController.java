package com.bookrecommender.client.controller;

import com.bookrecommender.common.dto.*;
import com.bookrecommender.common.enums.library.AddBookToLibResult;
import com.bookrecommender.common.extended_dto.BookInfo;
import com.bookrecommender.common.extended_dto.SuggestionCount;
import com.bookrecommender.common.extended_dto.SuggestionWithBooks;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class InfoLibroController
{
    @FXML private Label infoLibro, errorLabel, labelListRecensioni, labelRecensioni, labelListConsigliati, labelTopConsigliati;
    @FXML private Label labelNomeUtente;

    @FXML private ListView<String> listaConsigliati, listaRecensioni, listTopConsigliati;

    @FXML private ComboBox<String> comboLibrerie;

    @FXML private Button btnAggiungiLibro, btnToggleAggiungi;
    @FXML private VBox sectionAggiungiContent, mainFooterBox;
    @FXML private VBox sidebarBox;
    @FXML private Button btnReg, btnAcc, btnLeTueLib, btnLogout, btnRitorno;

    private LinkedList<Library> librerie;
    private BookInfo bookInfo;
    private Hashtable<String, Library> librerieMap = new Hashtable<>();

    @FXML
    private void initialize() throws RemoteException
    {
        setupListWrap(listaRecensioni);
        setupListWrap(listaConsigliati);
        setupListWrap(listTopConsigliati);

        if(LoginController.user == Utente.REGISTRATO)
        {
            labelNomeUtente.setText("Utente: " + LoginController.userId);
            sidebarBox.getChildren().removeAll(btnReg, btnAcc);
            mainFooterBox.setVisible(true);

            comboLibrerie.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {
                    LibrerieUtenteController.libreria = librerieMap.get(newVal);
                    btnAggiungiLibro.setDisable(false);
                }
            });

            registrato();
        }
        else
        {
            labelNomeUtente.setText("Ospite");
            sidebarBox.getChildren().removeAll(btnLeTueLib, btnLogout);
            mainFooterBox.setVisible(false);
            ospite();
        }
    }

    @FXML
    void toggleSectionAggiungi(ActionEvent event) {
        boolean currentlyVisible = sectionAggiungiContent.isVisible();
        sectionAggiungiContent.setVisible(!currentlyVisible);
        sectionAggiungiContent.setManaged(!currentlyVisible);

        if (!currentlyVisible) {
            btnToggleAggiungi.setText("▲ Nascondi sezione Aggiungi");
        } else {
            btnToggleAggiungi.setText("▼ Aggiungi a una Libreria");
        }
    }

    private void setupListWrap(ListView<String> listView) {
        listView.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setWrapText(true);
                    setPrefWidth(0);
                    maxWidthProperty().bind(listView.widthProperty().subtract(20));
                }
            }
        });
    }

    private void ospite() throws RemoteException
    {
        bookInfo = App.getInstance().bookRepository.getBookInfo(BenController.libro.id);
        infoLibro.setText(bookInfo.book.toStringInfo());
        caricaListeComuni();
    }

    private void registrato() throws RemoteException
    {
        librerie = App.getInstance().authedBookRepository.getMyLibrerie();
        bookInfo = App.getInstance().bookRepository.getBookInfo(BenController.libro.id);
        infoLibro.setText(bookInfo.book.toStringInfo());

        caricaListeComuni();

        // Carica la ComboBox
        comboLibrerie.setItems(
                FXCollections.observableArrayList(
                        librerie.stream()
                                .map(l -> l.name)
                                .collect(Collectors.toList())
                )
        );

        librerie.forEach(l->{
            librerieMap.put(l.name, l);
        });
    }

    private void caricaListeComuni() {
        listaRecensioni.setItems(
                FXCollections.observableArrayList(
                        bookInfo.ratings.stream()
                                .map(Rating::toStringInfo)
                                .collect(Collectors.toList())
                )
        );
        if(listaRecensioni.getItems().isEmpty()) labelListRecensioni.setVisible(true);

        listaConsigliati.setItems(
                FXCollections.observableArrayList(
                        bookInfo.suggestions.stream()
                                .map(SuggestionWithBooks::toStringInfo)
                                .collect(Collectors.toList())
                )
        );
        if(listaConsigliati.getItems().isEmpty()) labelListConsigliati.setVisible(true);

        verificheListeRecensioniConsigliati();
    }

    private void verificheListeRecensioniConsigliati()
    {
        if(!listaRecensioni.getItems().isEmpty())
        {
            labelRecensioni.setText("Stile: "+bookInfo.averageRatings.stile+"\nContenuto "+bookInfo.averageRatings.contenuto+
                    "\nGradevolezza "+bookInfo.averageRatings.gradevolezza+
                    "\nOriginalità "+bookInfo.averageRatings.originalita+
                    "\nEdizione "+bookInfo.averageRatings.edizione+
                    "\nFinale "+bookInfo.averageRatings.finale);
        } else {
            labelRecensioni.setText("Nessuna statistica disponibile.");
        }

        if(!bookInfo.suggestionCounts.isEmpty())
        {
            listTopConsigliati.setItems(
                    FXCollections.observableArrayList(
                            bookInfo.suggestionCounts.stream()
                                    .map(SuggestionCount::toStringInfo)
                                    .collect(Collectors.toList())
                    )
            );
        }
    }

    @FXML
    void btnClickAggiungiLibro(ActionEvent event) throws RemoteException
    {
        if (LibrerieUtenteController.libreria == null) return;

        AddBookToLibResult result = App.getInstance().authedBookRepository.aggiungiLibroALibreria(LibrerieUtenteController.libreria.id, BenController.libro.id);

        if(result == AddBookToLibResult.OK)
        {
            App.getInstance().changeScene("Libreria.fxml");
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

    @FXML
    void registrati(ActionEvent event) throws IOException {
        App.getInstance().changeScene("Registrazione.fxml");
    }

    @FXML
    void accedi(ActionEvent event) throws IOException {
        App.getInstance().changeScene("Login.fxml");
    }

    @FXML
    void btnClickCreaLib(ActionEvent event) throws IOException {
        App.getInstance().changeScene("CreaLibreria.fxml");
    }

    @FXML
    void btnClickLeTueLib(ActionEvent event) throws IOException {
        App.getInstance().changeScene("LibrerieUtente.fxml");
    }

    @FXML
    void btnClickLogout(ActionEvent event) throws RemoteException {
        LoginController.user = Utente.OSPITE;
        App.getInstance().authedBookRepository.logout();
        App.getInstance().changeScene("Benvenuto.fxml");
    }
}