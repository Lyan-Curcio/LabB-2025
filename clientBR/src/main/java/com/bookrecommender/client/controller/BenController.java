package com.bookrecommender.client.controller;

import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.bookrecommender.common.dto.Book;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class BenController {

    @FXML private VBox buttonList;
    @FXML private Button btnAcc, btnReg;    //bottoni che appaiono nella sezione ospite
    @FXML private Button btnCreaLib, btnRicercaLib, btnLeTueLib,btnLogout; //bottoni che appaiono nella sezione registrato
    @FXML private TextField tfRicerca;
    @FXML private TextField tfRicercaAnno;
    @FXML private SplitMenuButton tipiDiRicerca;
    @FXML private Label listLabel;
    @FXML private ListView<String> listaLibri;
    @FXML private Button btnInfoLibro;

    public static Book libro;


    private HashMap<String, Book> ricercaLibriMap = new HashMap<>();

    @FXML
    private void initialize()
    {
        btnInfoLibro.setVisible(false);
        if(LoginController.user == Utente.REGISTRATO)
        {
            listLabel.setText("registrato");
            buttonList.getChildren().removeAll(btnAcc, btnReg);
        }
        else
        {
            listLabel.setText("ospite");
            buttonList.getChildren().removeAll(btnCreaLib, btnRicercaLib, btnLeTueLib,btnLogout);
        }
    }

    private enum TipoDiRicerca {
        TITOLO,
        AUTORE,
        AUTORE_E_ANNO
    }
    private TipoDiRicerca tipoDiRicerca = TipoDiRicerca.TITOLO;

    //sezione per la selezione del tipo di ricerca
    @FXML
    void ricercaAutore(ActionEvent event)
    {
        tipoDiRicerca = TipoDiRicerca.AUTORE;
        tipiDiRicerca.setText("Autore");
        tfRicercaAnno.setDisable(true);
    }
    @FXML
    void ricercaTitolo(ActionEvent event)
    {
        tipoDiRicerca = TipoDiRicerca.TITOLO;
        tipiDiRicerca.setText("Titolo");
        tfRicercaAnno.setDisable(true);
    }
    @FXML
    void ricercaAutoreEAnno(ActionEvent event)
    {
        tipoDiRicerca = TipoDiRicerca.AUTORE_E_ANNO;
        tipiDiRicerca.setText("Autore e anno");
        tfRicercaAnno.setDisable(false);
    }
    //ricerca del libro
    @FXML
    void btnClickResearch(ActionEvent event)
    {
        listLabel.setText("");
        List<Book> libri = null;
        btnInfoLibro.setVisible(false);
        if(tfRicerca.getText().isEmpty())
        {
            listLabel.setText("Ricerca non valida");
            return;
        }
        try
        {
            if (tipoDiRicerca == TipoDiRicerca.TITOLO)
            {
                libri = App.getInstance().bookRepository.cercaLibroPerTitolo(tfRicerca.getText());
            }
            else if (tipoDiRicerca == TipoDiRicerca.AUTORE)
            {
                libri = App.getInstance().bookRepository.cercaLibroPerAutore(tfRicerca.getText());
            }
            else if (tipoDiRicerca == TipoDiRicerca.AUTORE_E_ANNO)
            {
                int anno = 0;
                try
                {
                    anno = Integer.parseInt(tfRicercaAnno.getText());
                }
                catch (NumberFormatException e)
                {
                    listaLibri.getItems().clear();
                    listLabel.setText("L'anno cercato non è valido!");
                    return;
                }
                if (anno < 0 || anno > LocalDate.now().getYear())
                {
                    listaLibri.getItems().clear();
                    listLabel.setText("L'anno cercato non è valido!");
                    return;
                }

                libri = App.getInstance().bookRepository.cercaLibroPerAutoreEAnno(tfRicerca.getText(), anno);
                listLabel.setText("");
            }
        }
        catch (RemoteException e)
        {
            listaLibri.getItems().clear();
            listLabel.setText("C'è stato un errore durante la ricerca!");
            e.printStackTrace();
            return;
        }

        if (libri == null || libri.isEmpty())
        {
            listaLibri.getItems().clear();
            listLabel.setText("Nessun libro trovato");
            return;
        }

        ricercaLibriMap.clear();

        libri.forEach(l -> {
            ricercaLibriMap.put(l.toStringInfo(), l);
        });

        listaLibri.setItems(
            FXCollections.observableArrayList(
                libri.stream()
                    .map(Book::toStringInfo)
                    .collect(Collectors.toList())
            )
        );


        listaLibri.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1)
            {
                libro = ricercaLibriMap.get(listaLibri.getSelectionModel().getSelectedItem());
                btnInfoLibro.setVisible(true);
            }
        });
    }

    @FXML
    void btnClickInfoLibro(ActionEvent event)
    {
        App.getInstance().changeScene("Valutazione.fxml");
    }

    //bottoni di navigazione se si è ospite
    @FXML
    void accedi(ActionEvent event) throws IOException
    {
        App m = App.getInstance();
        m.changeScene("Login.fxml");
    }

    @FXML
    void registrati(ActionEvent event) throws IOException
    {
        App m = App.getInstance();
        m.changeScene("Registrazione.fxml");
    }

    //bottoni di navigazione se si è registrato
    @FXML
    void btnClickCreaLib(ActionEvent event) throws IOException
    {
        App.getInstance().changeScene("CreaLibreria.fxml");
    }
    @FXML
    void btnClickRicercaLib(ActionEvent event) throws IOException
    {
        App.getInstance().changeScene("RicercheLibrerie.fxml");
    }
    @FXML
    void btnClickLeTueLib(ActionEvent event) throws IOException
    {
        App.getInstance().changeScene("LibrerieUtente.fxml");
    }
    @FXML
    void btnClickLogout(ActionEvent event) throws IOException
    {
        LoginController.user = Utente.OSPITE;
        App.getInstance().changeScene("Benvenuto.fxml");
    }
}
