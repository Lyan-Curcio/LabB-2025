package com.bookrecommender.client.controller;

import com.bookrecommender.common.dto.*;
import com.bookrecommender.common.enums.library.AddBookToLibResult;
import com.bookrecommender.common.extended_dto.BookInfo;
import com.bookrecommender.common.extended_dto.SuggestionCount;
import com.bookrecommender.common.extended_dto.SuggestionWithBooks;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Controller JavaFX che gestisce la schermata di visualizzazione delle informazioni dettagliate di un libro.
 * <p>
 * Mostra:
 * <ul>
 *   <li>Informazioni generali del libro</li>
 *   <li>Recensioni, consigliati e dati aggregati</li>
 *   <li>Sezione di aggiunta del libro a una libreria (solo utenti registrati)</li>
 * </ul>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class InfoLibroController
{
    /** Label informative e di supporto alla UI */
    @FXML private Label infoLibro, errorLabel, labelListRecensioni, labelRecensioni,
        labelListConsigliati, labelTopConsigliati, labelNomeUtente;

    /** Liste per recensioni, consigliati e top consigliati */
    @FXML private ListView<String> listaConsigliati, listaRecensioni, listTopConsigliati;

    /** ComboBox contenente le librerie dell'utente */
    @FXML private ComboBox<String> comboLibrerie;

    /** Pulsanti di interazione */
    @FXML private Button btnAggiungiLibro, btnToggleAggiungi;
    /** Pulsanti di interazione */
    @FXML private Button btnReg, btnAcc, btnLeTueLib, btnLogout, btnRitorno;

    /** Contenitori grafici */
    @FXML private VBox sectionAggiungiContent, mainFooterBox, sidebarBox;

    /** Librerie dell'utente registrato */
    private LinkedList<Library> librerie;

    /** Informazioni estese del libro visualizzato */
    private BookInfo bookInfo;

    /**
     * HashMap a supporto della selezione della libreria per l'aggiunta del libro.
     * nome libreria → oggetto Library
     */
    private final HashMap<String, Library> librerieMap = new HashMap<>();

    /**
     * Metodo di inizializzazione.
     * Configura la UI in base al tipo di utente (ospite o registrato)
     * e inizializza le liste.
     *
     * @throws RemoteException in caso di errore di comunicazione RMI
     */
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

    /**
     * Mostra o nasconde la sezione per aggiungere il libro a una libreria.
     *
     * @param event evento di click del pulsante
     */
    @FXML
    void toggleSectionAggiungi(ActionEvent event) {
        boolean currentlyVisible = sectionAggiungiContent.isVisible();
        sectionAggiungiContent.setVisible(!currentlyVisible);
        sectionAggiungiContent.setManaged(!currentlyVisible);

        btnToggleAggiungi.setText(
            !currentlyVisible
                ? "▲ Nascondi sezione Aggiungi"
                : "▼ Aggiungi a una Libreria"
        );
    }

    /**
     * Configura una ListView per supportare il testo multilinea.
     *
     * @param listView lista da configurare
     */
    private void setupListWrap(ListView<String> listView)
    {
        listView.setCellFactory(param -> new ListCell<>()
        {
            @Override
            protected void updateItem(String item, boolean empty)
            {
                super.updateItem(item, empty);
                if (empty || item == null)
                {
                    setText(null);
                    setGraphic(null);
                }
                else
                {
                    setText(item);
                    setWrapText(true);
                    setPrefWidth(0);
                    maxWidthProperty().bind(listView.widthProperty().subtract(20));
                }
            }
        });
    }

    /**
     * Inizializza la schermata per un utente ospite.
     *
     * @throws RemoteException errore di comunicazione RMI
     */
    private void ospite() throws RemoteException
    {
        bookInfo = App.getInstance().bookRepository.getBookInfo(BenController.libro.id);
        if (bookInfo.book.categorie.length == 0)
        {
            infoLibro.setText(bookInfo.book.toStringInfo()+"\n - " + bookInfo.book.editore + "\n");
            caricaListeComuni();
        }
        else
        {
            infoLibro.setText(bookInfo.book.toStringInfo()+"\n - " + bookInfo.book.editore + "\n - " + Arrays.toString(bookInfo.book.categorie));
            caricaListeComuni();
        }

    }

    /**
     * Inizializza la schermata per un utente registrato, caricando anche le librerie personali.
     *
     * @throws RemoteException errore di comunicazione RMI
     */
    private void registrato() throws RemoteException
    {
        librerie = App.getInstance().authedBookRepository.getMyLibrerie();
        bookInfo = App.getInstance().bookRepository.getBookInfo(BenController.libro.id);
        infoLibro.setText(bookInfo.book.toStringInfo());

        caricaListeComuni();

        comboLibrerie.setItems(
            FXCollections.observableArrayList(
                librerie.stream()
                    .map(l -> l.name)
                    .collect(Collectors.toList())
            )
        );

        librerie.forEach(l -> librerieMap.put(l.name, l));
    }

    /**
     * Carica le liste per recensioni, consigliati e top consigliati.
     */
    private void caricaListeComuni() {
        listaRecensioni.setItems(
            FXCollections.observableArrayList(
                bookInfo.ratings.stream()
                    .map(Rating::toStringInfo)
                    .collect(Collectors.toList())
            )
        );

        if(listaRecensioni.getItems().isEmpty())
            labelListRecensioni.setVisible(true);

        listaConsigliati.setItems(
            FXCollections.observableArrayList(
                bookInfo.suggestions.stream()
                    .map(SuggestionWithBooks::toStringInfo)
                    .collect(Collectors.toList())
            )
        );

        if(listaConsigliati.getItems().isEmpty())
            labelListConsigliati.setVisible(true);

        verificheListeRecensioniConsigliati();
    }

    /**
     * Aggiorna le statistiche delle recensioni e la lista dei libri più consigliati.
     */
    private void verificheListeRecensioniConsigliati()
    {
        if(!listaRecensioni.getItems().isEmpty())
        {
            labelListRecensioni.setText("");

            labelRecensioni.setText(
                "Hanno valutato questo libro in " + bookInfo.ratings.size() +
                    "\nStile: " + bookInfo.averageRatings.stile +
                    "\nContenuto: " + bookInfo.averageRatings.contenuto +
                    "\nGradevolezza: " + bookInfo.averageRatings.gradevolezza +
                    "\nOriginalità: " + bookInfo.averageRatings.originalita +
                    "\nEdizione: " + bookInfo.averageRatings.edizione +
                    "\nFinale: " + bookInfo.averageRatings.finale
            );
        }
        else {
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

    /**
     * Aggiunge il libro selezionato alla libreria scelta dall'utente.
     *
     * @param event evento di click
     * @throws RemoteException errore di comunicazione RMI
     */
    @FXML
    void btnClickAggiungiLibro(ActionEvent event) throws RemoteException
    {
        if (LibrerieUtenteController.libreria == null) return;

        AddBookToLibResult result =
            App.getInstance().authedBookRepository
                .aggiungiLibroALibreria(
                    LibrerieUtenteController.libreria.id,
                    BenController.libro.id
                );

        if(result == AddBookToLibResult.OK)
            App.getInstance().changeScene("Libreria.fxml");
        else
            errorLabel.setText(result.getMessage());
    }

    /**
     * Torna alla schermata principale
     *
     * @param event evento di click
     */
    @FXML
    private void btnClickReturn(ActionEvent event)
    {
        App.getInstance().changeScene("Benvenuto.fxml");
    }

    /**
     * Apre la schermata di registrazione
     *
     * @param event evento di click
     */
    @FXML
    void registrati(ActionEvent event) {
        App.getInstance().changeScene("Registrazione.fxml");
    }

    /**
     * Apre la schermata di login
     *
     * @param event evento di click
     */
    @FXML
    void accedi(ActionEvent event) {
        App.getInstance().changeScene("Login.fxml");
    }

    /**
     * Apre la schermata delle librerie dell'utente
     *
     * @param event evento di click
     */
    @FXML
    void btnClickLeTueLib(ActionEvent event) {
        App.getInstance().changeScene("LibrerieUtente.fxml");
    }

    /**
     * Effettua il logout dell'utente loggato.
     *
     * @param event evento di click
     * @throws RemoteException errore di comunicazione RMI
     */
    @FXML
    void btnClickLogout(ActionEvent event) throws RemoteException {
        LoginController.user = Utente.OSPITE;
        App.getInstance().authedBookRepository.logout();
        App.getInstance().changeScene("Benvenuto.fxml");
    }
}
