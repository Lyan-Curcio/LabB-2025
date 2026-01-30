package com.bookrecommender.common.dto;

import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe DTO (Data Transfer Object) che rappresenta l'entità di un Libro.
 * <p>
 * Questa classe è immutabile (i campi sono <code>final</code>) e implementa <code>Serializable</code>
 * per permettere il trasferimento dei dati tra il server e il client tramite RMI.
 * Include logica di parsing per costruire l'oggetto direttamente da un <code>ResultSet</code> SQL.
 * </p>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class Book implements Serializable {

    /** Versione della classe per la serializzazione. */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Identificativo univoco del libro nel database. */
    public final int id;

    /** Titolo completo del libro. */
    public final String titolo;

    /** Array contenente i nomi degli autori del libro. */
    public final String[] autori;

    /** Anno di pubblicazione del libro. */
    public final int annoPubblicazione;

    /** Casa editrice che ha pubblicato il libro. */
    public final String editore;

    /** Array delle categorie o generi a cui appartiene il libro. */
    public final String[] categorie;

    /**
     * Costruisce un nuovo oggetto {@link Book} con i dati specificati.
     * <p>
     * Questo costruttore viene utilizzato quando i dati sono già stati elaborati
     * e non provengono direttamente da una query SQL grezza.
     * </p>
     *
     * @param titolo     il titolo del libro
     * @param autori     un array di stringhe contenente gli autori
     * @param anno       l'anno di pubblicazione
     * @param editore    il nome dell'editore
     * @param categorie  un array di stringhe contenente le categorie
     */
    public Book(String titolo, String[] autori, int anno, String editore, String[] categorie) {
        this.id = -1;
        this.titolo = titolo;
        this.autori = autori;
        this.annoPubblicazione = anno;
        this.editore = editore;
        this.categorie = categorie;
    }

    /**
     * Costruisce un oggetto {@link Book} estraendo i dati da un <code>ResultSet</code> SQL.
     * <p>
     * Questo costruttore effettua il parsing automatico delle colonne database e converte
     * le stringhe separate da virgola (per autori e categorie) in array.
     * In caso di <code>SQLException</code>, viene generato un oggetto vuoto/di default e l'errore viene stampato su <code>System.err</code>.
     * </p>
     *
     * @param rs il <code>ResultSet</code> posizionato sulla riga da convertire
     */
    public Book(ResultSet rs) {
        int _id;
        String _titolo;
        String[] _autori;
        int _annoPubblicazione;
        String _editore;
        String[] _categorie;

        try {
            _id = rs.getInt("id");
            _titolo = rs.getString("titolo");
            _autori = autoriParser(rs.getString("autori"));
            _annoPubblicazione = rs.getInt("anno_pubblicazione");
            _editore = rs.getString("editore");
            _categorie = categorieParser(rs.getString("categorie"));
        }
        catch (SQLException e) {
            System.err.println("Impossibile costruire un 'Book' con il 'ResultSet': " + rs);
            _id = 0;
            _titolo = "";
            _autori = new String[0];
            _annoPubblicazione = 0;
            _editore = "";
            _categorie = new String[0];
        }

        this.id = _id;
        this.titolo = _titolo;
        this.autori = _autori;
        this.annoPubblicazione = _annoPubblicazione;
        this.editore = _editore;
        this.categorie = _categorie;
    }

    /**
     * Restituisce una rappresentazione dettagliata dell'oggetto utile per il debugging.
     *
     * @return una stringa contenente tutti i dati del libro
     */
    public String toStringDebug() {
        return id + ": " + titolo +
                "\n\t" + String.join(", ", autori) +
                "\n\t" + annoPubblicazione +
                "\n\t" + editore +
                "\n\t" + String.join(", ", categorie);
    }

    /**
     * Restituisce una stringa formattata sintetica per la visualizzazione all'utente.
     * <p>
     * Formato: "Titolo - Da: Autore1, Autore2 - (Anno)"
     * </p>
     *
     * @return una stringa formattata con titolo, autori e anno
     */
    public String toStringInfo() {
        return titolo + "\n - " + String.join(", ", autori) + "\n - (" + annoPubblicazione + ")";
    }

    /**
     * Metodo di utilità interno per convertire la stringa degli autori dal DB in un array.
     *
     * @param autoriStr la stringa proveniente dal DB (es. "Autore1,Autore2")
     * @return un array di stringhe contenente i singoli autori
     */
    private String[] autoriParser(String autoriStr) {
        if (autoriStr == null || autoriStr.isEmpty()) {
            return new String[0];
        }

        return autoriStr.split(",");
    }

    /**
     * Metodo di utilità interno per convertire la stringa delle categorie dal DB in un array.
     *
     * @param categorieStr la stringa proveniente dal DB (es. "Fantasy,Horror")
     * @return un array di stringhe contenente le singole categorie
     */
    private String[] categorieParser(String categorieStr) {
        if (categorieStr == null || categorieStr.isEmpty()) {
            return new String[0];
        }

        return categorieStr.split(",");
    }
}