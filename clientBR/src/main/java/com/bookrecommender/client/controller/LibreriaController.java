package com.bookrecommender.client.controller;

import com.bookrecommender.common.dto.Book;
import com.bookrecommender.common.dto.Rating;
import com.bookrecommender.common.extended_dto.SuggestionWithBooks;
import com.bookrecommender.common.enums.library.RemoveBookFromLibResult;
import com.bookrecommender.common.enums.rating.DeleteRatingResult;
import com.bookrecommender.common.enums.suggestion.RemoveSuggestionResult;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Controller JavaFX per gestire la libreria dell'utente.
 * <p>
 * Questo controller permette di visualizzare i libri presenti nella libreria, aggiungere suggerimenti,
 * valutare libri, rimuovere libri o suggerimenti e visualizzare informazioni dettagliate sui libri.
 * </p>
 */
public class LibreriaController {

    /** Pulsante per consigliare un libro ad altri utenti. */
    @FXML private Button btnConsiglia;

    /** Pulsante per valutare un libro selezionato. */
    @FXML private Button btnValuta;

    /** Pulsante per visualizzare informazioni dettagliate su un libro. */
    @FXML private Button btnInfoLibro;

    /** Pulsante per rimuovere un suggerimento associato a un libro. */
    @FXML private Button btnRimuoviConsiglio;

    /** Pulsante per rimuovere un libro dalla libreria. */
    @FXML private Button btnRimuoviLibro;

    /** Pulsante per rimuovere la valutazione di un libro. */
    @FXML private Button btnRimuoviRecensione;

    /** Lista dei libri consigliati relativi al libro selezionato. */
    @FXML private ListView<String> libriConsigliati;

    /** Lista dei libri presenti nella libreria dell'utente. */
    @FXML private ListView<String> listaLibriLibreria;

    /** Titolo della libreria corrente. */
    @FXML private Label nomeLibreria;

    /** Label per la visualizzazione di eventuali errori. */
    @FXML private Label errorLabel;

    /** Label che mostra la recensione dell'utente per il libro selezionato. */
    @FXML private Label recensione;

    /** Label che mostra il nome dell'utente loggato. */
    @FXML private Label labelNomeUtente;


    /** Hash per la ricerca rapida dei libri presenti nella libreria tramite stringa descrittiva. */
    private HashMap<String, Book> ricercaMapLibri = new HashMap<>();

    /** Mappa per la ricerca rapida dei suggerimenti tramite stringa descrittiva. */
    private HashMap<String, SuggestionWithBooks> MapSuggerimenti = new HashMap<>();

    /** Lista dei suggerimenti relativi al libro selezionato. */
    LinkedList<SuggestionWithBooks> consigliati;

    /** Valutazione dell'utente per il libro selezionato. */
    private Rating valutazione = null;

    /** Flag per confermare l'eliminazione di un libro dalla libreria. */
    private boolean confermaEliminazione = false;

    /** Suggerimento selezionato nella lista dei libri consigliati. */
    private SuggestionWithBooks consiglio;

    /** Libro attualmente selezionato nella lista della libreria. */
    public static Book libro;

    /** Libro consigliato selezionato. */
    public static Book libroConsigliato;

    /** Flag per indicare se l'utente sta consigliando un libro. */
    public static boolean consigliando = false;

    /**
     * Inizializza il controller, caricando i libri dalla libreria dell'utente
     * e impostando i listener per le selezioni nella UI.
     *
     * @throws RemoteException in caso di errore nella comunicazione con il repository remoto.
     */
    @FXML
    private void initialize() throws RemoteException {
        LinkedList<Book> libri;
        nomeLibreria.setText(LibrerieUtenteController.libreria.name);
        libri = App.getInstance().authedBookRepository.getLibriFromLibreria(LibrerieUtenteController.libreria.id);

        ricercaMapLibri.clear();

        libri.forEach(l -> {
            ricercaMapLibri.put(l.toStringInfo(), l);
        });
        listaLibriLibreria.setItems(
            FXCollections.observableArrayList(
                libri.stream()
                    .map(Book::toStringInfo)
                    .collect(Collectors.toList())
            )
        );

        if (LoginController.user == Utente.REGISTRATO) {
            labelNomeUtente.setText("Utente: " + LoginController.userId);
        }

        listaLibriLibreria.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                libro = ricercaMapLibri.get(listaLibriLibreria.getSelectionModel().getSelectedItem());
                confermaEliminazione = false;
                errorLabel.setText("");

                btnRimuoviLibro.setVisible(true);
                btnInfoLibro.setVisible(true);
                try {
                    btnConsiglia.setVisible(true);
                    consigliati = App.getInstance().authedBookRepository.getMySuggerimenti(libro.id);
                } catch (RemoteException e) {
                    libriConsigliati.getItems().clear();
                    errorLabel.setText("c'è stato un errore durante la consulta dei suggerimenti");
                    e.printStackTrace();
                    return;
                }

                MapSuggerimenti.clear();
                consigliati.forEach(sug -> {
                    MapSuggerimenti.put(sug.toStringInfo(), sug);
                });
                libriConsigliati.setItems(
                    FXCollections.observableArrayList(
                        consigliati.stream()
                            .map(SuggestionWithBooks::toStringInfo)
                            .collect(Collectors.toList())
                    )
                );
                try {
                    valutazione = App.getInstance().authedBookRepository.getMyValutazione(libro.id);
                    if (valutazione == null || valutazione.toStringInfo().isEmpty()) {
                        btnRimuoviRecensione.setVisible(false);
                        btnValuta.setVisible(true);
                        recensione.setText("");
                    } else {
                        btnValuta.setVisible(false);
                        recensione.setText(valutazione.toStringInfo());
                        btnRimuoviRecensione.setVisible(true);
                    }
                } catch (RemoteException e) {
                    errorLabel.setText("c'è stato un errore durante l'acquisizione della recensione");
                }
            }
        });
        libriConsigliati.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                try {
                    consiglio = MapSuggerimenti.get(libriConsigliati.getSelectionModel().getSelectedItem());
                    libroConsigliato = consiglio.suggestedBook;
                    btnRimuoviConsiglio.setVisible(true);
                } catch (NullPointerException e) {
                    btnRimuoviConsiglio.setVisible(false);
                }
            }
        });

        listaLibriLibreria.setCellFactory(param -> new javafx.scene.control.ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item);
                    setWrapText(true); // Abilita testo a capo
                    setPrefWidth(0);   // Reset larghezza
                    maxWidthProperty().bind(listaLibriLibreria.widthProperty().subtract(20)); // Adatta alla larghezza della lista
                }
            }
        });

        // --- AGGIUNGI QUESTO PER LISTA CONSIGLIATI ---
        libriConsigliati.setCellFactory(param -> new javafx.scene.control.ListCell<String>() {
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
                    maxWidthProperty().bind(libriConsigliati.widthProperty().subtract(20));
                }
            }
        });
    }

    /**
     * Azione per aggiungere un libro consigliato.
     *
     * @param event evento generato dal pulsante
     */
    @FXML
    void aggiungiConsigliato(ActionEvent event) {
        consigliando = true;
        App.getInstance().changeScene("Benvenuto.fxml");
    }

    /**
     * Azione per aggiungere una valutazione a un libro.
     *
     * @param event evento generato dal pulsante
     */
    @FXML
    void aggiungiValutazione(ActionEvent event) {
        App.getInstance().changeScene("Valutazione.fxml");
    }

    /**
     * Rimuove il suggerimento relativo al libro selezionato.
     *
     * @param event evento generato dal pulsante
     * @throws RemoteException in caso di errore nella comunicazione remota
     */
    @FXML
    void rimuoviConsiglio(ActionEvent event) throws RemoteException {
        RemoveSuggestionResult result = App.getInstance().authedBookRepository.rimuoviSuggerimentoLibro(libro.id, libroConsigliato.id);
        if (result == RemoveSuggestionResult.OK) {
            App.getInstance().changeScene("Libreria.fxml");
        } else {
            errorLabel.setText(result.getMessage());
        }
    }

    /**
     * Rimuove il libro selezionato dalla libreria dell'utente.
     * Richiede conferma prima dell'eliminazione definitiva.
     *
     * @param event evento generato dal pulsante
     * @throws RemoteException in caso di errore nella comunicazione remota
     */
    @FXML
    void rimuoviLibro(ActionEvent event) throws RemoteException {
        if (!confermaEliminazione) {
            errorLabel.setText("ATTENZIONE! Se il libro non è in nessun'altra libreria le sue recensioni e i suoi consigliati verranno eliminati");
            confermaEliminazione = true;
        } else {
            RemoveBookFromLibResult result = App.getInstance().authedBookRepository.rimuoviLibroDaLibreria(LibrerieUtenteController.libreria.id, libro.id);
            if (result == RemoveBookFromLibResult.OK) {
                App.getInstance().changeScene("Libreria.fxml");
            } else {
                errorLabel.setText(result.getMessage());
            }
        }
    }

    /**
     * Rimuove la valutazione dell'utente per il libro selezionato.
     *
     * @param event evento generato dal pulsante
     * @throws RemoteException in caso di errore nella comunicazione remota
     */
    @FXML
    void rimuoviValutazione(ActionEvent event) throws RemoteException {
        DeleteRatingResult result = App.getInstance().authedBookRepository.rimuoviValutazioneLibro(libro.id);
        if (result == DeleteRatingResult.OK) {
            App.getInstance().changeScene("Libreria.fxml");
        } else {
            errorLabel.setText(result.getMessage());
        }
    }

    /**
     * Mostra le informazioni dettagliate sul libro selezionato.
     *
     * @param event evento generato dal pulsante
     */
    @FXML
    void btnClickInfoLibro(ActionEvent event) {
        BenController.libro = libro;
        App.getInstance().changeScene("InformazioniLibro.fxml");
    }

    /**
     * Naviga alla scena che mostra tutte le librerie dell'utente.
     *
     * @param event evento generato dal pulsante
     */
    @FXML
    void btnClickLeTueLib(ActionEvent event) {
        App.getInstance().changeScene("LibrerieUtente.fxml");
    }

    /**
     * Effettua il logout dell'utente e ritorna alla scena di benvenuto.
     *
     * @param event evento generato dal pulsante
     * @throws RemoteException in caso di errore durante il logout
     */
    @FXML
    void btnClickLogout(ActionEvent event) throws RemoteException {
        LoginController.user = Utente.OSPITE;
        App.getInstance().authedBookRepository.logout();
        App.getInstance().changeScene("Benvenuto.fxml");
    }

    /**
     * Naviga alla scena per la ricerca dei libri.
     *
     * @param event evento generato dal pulsante
     */
    @FXML
    void btnClickRicercaLibri(ActionEvent event) {
        App.getInstance().changeScene("Benvenuto.fxml");
    }
}
