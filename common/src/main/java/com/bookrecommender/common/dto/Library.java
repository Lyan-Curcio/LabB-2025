package com.bookrecommender.common.dto;

import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe DTO (Data Transfer Object) che rappresenta una libreria personalizzata di un utente.
 * <p>
 * Una libreria è una collezione logica di libri creata da un utente (es. "Libri letti", "Preferiti").
 * La classe è immutabile e implementa <code>Serializable</code> per il trasferimento dati via rete.
 * </p>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class Library implements Serializable {

    /** Versione della classe per la serializzazione. */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Identificativo univoco della libreria nel database. */
    public final int id;

    /** Nome assegnato alla libreria dall'utente. */
    public final String name;

    /** Identificativo (username) dell'utente proprietario della libreria. */
    public final String userId;

    /**
     * Costruisce una nuova {@link Library} con i dati specificati.
     * <p>
     * L'ID viene inizializzato a -1, indicando che l'oggetto non è ancora stato persistito o l'ID non è rilevante nel contesto corrente.
     * </p>
     *
     * @param name   il nome della libreria
     * @param userId l'ID dell'utente proprietario
     */
    public Library(String name, String userId) {
        id = -1;
        this.name = name;
        this.userId = userId;
    }

    /**
     * Costruisce un oggetto {@link Library} estraendo i dati da un <code>ResultSet</code> SQL.
     * <p>
     * In caso di <code>SQLException</code> durante la lettura, viene generato un oggetto vuoto
     * e l'errore viene stampato su <code>System.err</code>.
     * </p>
     *
     * @param rs il <code>ResultSet</code> posizionato sulla riga della libreria da leggere
     */
    public Library(ResultSet rs) {
        int _id;
        String _name;
        String _userId;

        try {
            _id = rs.getInt("id");
            _name = rs.getString("nome");
            _userId = rs.getString("userid");
        }
        catch (SQLException e) {
            System.err.println("Impossibile costruire una 'Library' con il 'ResultSet': " + rs);
            _id = 0;
            _name = "";
            _userId = "";
        }

        this.id = _id;
        this.name = _name;
        this.userId = _userId;
    }

    /**
     * Restituisce una stringa formattata sintetica per la visualizzazione all'utente.
     *
     * @return una stringa formattata
     */
    public String toStringInfo() {
        return "\"" + name + "\" - Di: " + userId;
    }

    /**
     * Restituisce una rappresentazione stringa della libreria per scopi di debug.
     *
     * @return una stringa contenente ID, nome e proprietario
     */
    public String toStringDebug() {
        return id + ": " + name + " di " + userId;
    }
}