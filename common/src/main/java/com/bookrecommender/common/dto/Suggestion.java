package com.bookrecommender.common.dto;

import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe DTO (Data Transfer Object) che rappresenta un suggerimento di correlazione tra due libri.
 * <p>
 * Un suggerimento indica che un utente consiglia il libro "suggestedBookId" a chi ha letto o apprezzato
 * il libro "mainBookId".
 * Implementa <code>Serializable</code> per il trasferimento dati via rete.
 * </p>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class Suggestion implements Serializable {
    /** Versione della classe per la serializzazione. */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Identificativo univoco del suggerimento nel database. */
    public final int id;

    /** Identificativo dell'utente che ha creato il suggerimento. */
    public final String userId;

    /** ID del libro sorgente (il libro sul quale viene dato il consiglio). */
    public final int mainBookId;

    /** ID del libro consigliato. */
    public final int suggestedBookId;

    /**
     * Costruisce un nuovo oggetto {@link Suggestion} con i dati specificati.
     *
     * @param userId          l'ID dell'utente autore del consiglio
     * @param mainBookId      l'ID del libro sorgente
     * @param suggestedBookId l'ID del libro consigliato
     */
    public Suggestion(String userId, int mainBookId, int suggestedBookId) {
        id = -1;
        this.userId = userId;
        this.mainBookId = mainBookId;
        this.suggestedBookId = suggestedBookId;
    }

    /**
     * Costruisce un oggetto {@link Suggestion} estraendo i dati da un <code>ResultSet</code> SQL.
     * <p>
     * In caso di <code>SQLException</code> durante la lettura, viene generato un oggetto vuoto
     * con valori di default e l'errore viene stampato su <code>System.err</code>.
     * </p>
     *
     * @param rs il <code>ResultSet</code> posizionato sulla riga del suggerimento da leggere
     */
    public Suggestion(ResultSet rs) {
        int _id;
        String _userId;
        int _mainBookId;
        int _suggestedBookId;

        try {
            _id = rs.getInt("id");
            _userId = rs.getString("userid");
            _mainBookId = rs.getInt("libro_sorgente_id");
            _suggestedBookId = rs.getInt("libro_consigliato_id");
        }
        catch (SQLException e) {
            System.err.println("Impossibile costruire una 'Suggestion' con il 'ResultSet': " + rs);
            _id = 0;
            _userId = "";
            _mainBookId = 0;
            _suggestedBookId = 0;
        }

        this.id = _id;
        this.userId = _userId;
        this.mainBookId = _mainBookId;
        this.suggestedBookId = _suggestedBookId;
    }

    /**
     * Restituisce una rappresentazione stringa del suggerimento per scopi di debug.
     *
     * @return una stringa con ID, utente e la relazione tra i due libri
     */
    public String toStringDebug() {
        return id + ": da " + userId +
                "\n\t" + mainBookId + " -> " + suggestedBookId;
    }
}