package com.bookrecommender.common.dto;

import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe DTO (Data Transfer Object) che rappresenta una valutazione rilasciata da un utente per un libro.
 * <p>
 * Contiene sia i punteggi numerici (da 1 a 5) per diverse categorie (stile, contenuto, ecc.),
 * sia le note testuali opzionali associate.
 * La classe è immutabile e implementa <code>Serializable</code> per il trasferimento dati via rete.
 * </p>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class Rating implements Serializable {

    /** Versione della classe per la serializzazione. */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Identificativo univoco della valutazione nel database. */
    public final int id;

    /** Identificativo del libro a cui si riferisce la valutazione. */
    public final int libroId;

    /** Identificativo dell'utente che ha rilasciato la valutazione. */
    public final String userId;

    // Punteggi 1-5

    /** Punteggio assegnato allo stile di scrittura (range 1-5). */
    public final int stile;

    /** Punteggio assegnato al contenuto del libro (range 1-5). */
    public final int contenuto;

    /** Punteggio assegnato alla gradevolezza generale (range 1-5). */
    public final int gradevolezza;

    /** Punteggio assegnato all'originalità dell'opera (range 1-5). */
    public final int originalita;

    /** Punteggio assegnato alla qualità dell'edizione (range 1-5). */
    public final int edizione;

    /** Punteggio finale calcolato come media arrotondata dei 5 criteri precedenti. */
    public final int finale;

    // Note testuali (max 256 char)

    /** Nota testuale relativa allo stile (max 256 caratteri). */
    public final String noteStile;

    /** Nota testuale relativa al contenuto (max 256 caratteri). */
    public final String noteContenuto;

    /** Nota testuale relativa alla gradevolezza (max 256 caratteri). */
    public final String noteGradevolezza;

    /** Nota testuale relativa all'originalità (max 256 caratteri). */
    public final String noteOriginalita;

    /** Nota testuale relativa all'edizione (max 256 caratteri). */
    public final String noteEdizione;

    /** Nota testuale finale complessiva (max 256 caratteri). */
    public final String noteFinale;

    /**
     * Costruisce una nuova {@link Rating} con i punteggi e le note specificati.
     * <p>
     * Il punteggio <code>finale</code> viene calcolato automaticamente come media arrotondata
     * dei 5 parametri numerici passati.
     * </p>
     *
     * @param libroId          l'ID del libro valutato
     * @param userId           l'ID dell'utente valutatore
     * @param stile            punteggio stile (1-5)
     * @param contenuto        punteggio contenuto (1-5)
     * @param gradevolezza     punteggio gradevolezza (1-5)
     * @param originalita      punteggio originalità (1-5)
     * @param edizione         punteggio edizione (1-5)
     * @param noteStile        commento sullo stile
     * @param noteContenuto    commento sul contenuto
     * @param noteGradevolezza commento sulla gradevolezza
     * @param noteOriginalita  commento sull'originalità
     * @param noteEdizione     commento sull'edizione
     * @param noteFinale       commento finale
     */
    public Rating(
            int libroId, String userId,
            int stile, int contenuto, int gradevolezza, int originalita, int edizione,
            String noteStile, String noteContenuto, String noteGradevolezza, String noteOriginalita, String noteEdizione, String noteFinale
    )
    {
        this.id = -1;
        this.libroId = libroId;
        this.userId = userId;

        this.stile = stile;
        this.contenuto = contenuto;
        this.gradevolezza = gradevolezza;
        this.originalita = originalita;
        this.edizione = edizione;
        this.finale = Math.round((stile + contenuto + gradevolezza + originalita + edizione) / 5.0f);

        this.noteStile = noteStile;
        this.noteContenuto = noteContenuto;
        this.noteGradevolezza = noteGradevolezza;
        this.noteOriginalita = noteOriginalita;
        this.noteEdizione = noteEdizione;
        this.noteFinale = noteFinale;
    }

    /**
     * Costruisce un oggetto {@link Rating} estraendo i dati da un <code>ResultSet</code> SQL.
     * <p>
     * Questo costruttore recupera i punteggi e le note dalle colonne del database e ricalcola
     * il voto finale basandosi sui dati estratti.
     * In caso di <code>SQLException</code>, viene generato un oggetto vuoto/di default e l'errore stampato su <code>System.err</code>.
     * </p>
     *
     * @param rs il <code>ResultSet</code> posizionato sulla riga della valutazione da leggere
     */
    public Rating(ResultSet rs) {
        int _id, _libroId;
        String _userId;
        int _stile, _contenuto, _gradevolezza, _originalita, _edizione, _finale;
        String _noteStile, _noteContenuto, _noteGradevolezza, _noteOriginalita, _noteEdizione, _noteFinale;
        try {
            _id = rs.getInt("id");
            _libroId = rs.getInt("libro_id");
            _userId = rs.getString("userid");

            _stile = rs.getInt("stile");
            _contenuto = rs.getInt("contenuto");
            _gradevolezza = rs.getInt("gradevolezza");
            _originalita = rs.getInt("originalita");
            _edizione = rs.getInt("edizione");
            _finale = rs.getInt("finale");

            _noteStile = rs.getString("note_stile");
            _noteContenuto = rs.getString("note_contenuto");
            _noteGradevolezza = rs.getString("note_gradevolezza");
            _noteOriginalita = rs.getString("note_originalita");
            _noteEdizione = rs.getString("note_edizione");
            _noteFinale = rs.getString("note_finale");
        }
        catch (SQLException e) {
            System.err.println("Impossibile costruire una 'Valutazione' con il 'ResultSet': " + rs);
            _id = 0;
            _libroId = 0;
            _userId = "";

            _stile = 0;
            _contenuto = 0;
            _gradevolezza = 0;
            _originalita = 0;
            _edizione = 0;
            _finale = 0;

            _noteStile = "";
            _noteContenuto = "";
            _noteGradevolezza = "";
            _noteOriginalita = "";
            _noteEdizione = "";
            _noteFinale = "";
        }

        this.id = _id;
        this.libroId = _libroId;
        this.userId = _userId;

        this.stile = _stile;
        this.contenuto = _contenuto;
        this.gradevolezza = _gradevolezza;
        this.originalita = _originalita;
        this.edizione = _edizione;
        this.finale = _finale;

        this.noteStile = _noteStile;
        this.noteContenuto = _noteContenuto;
        this.noteGradevolezza = _noteGradevolezza;
        this.noteOriginalita = _noteOriginalita;
        this.noteEdizione = _noteEdizione;
        this.noteFinale = _noteFinale;
    }

    /**
     * Restituisce una rappresentazione dettagliata della valutazione per scopi di debug.
     *
     * @return una stringa contenente tutti i voti parziali e le relative note
     */
    public String toStringDebug() {
        return id + ": Da '" + userId + "' per '" + libroId + "'" +
                "\n\tStile " + stile + " " + noteStile +
                "\n\tContenuto " + contenuto +
                " " + noteContenuto +
                "\n\tGradevolezza " + gradevolezza + " " + noteGradevolezza +
                "\n\tOriginalità " + originalita + " " + noteOriginalita +
                "\n\tEdizione " + edizione + " " + noteEdizione +
                "\n\tFinale " + finale +
                " " + noteFinale;
    }

    /**
     * Restituisce una stringa sintetica della valutazione per la visualizzazione all'utente.
     *
     * @return una stringa formattata con l'autore della recensione e i dettagli dei voti
     */
    public String toStringInfo() {
        return "Da " + userId +
                "\nStile " + stile + " " + noteStile +
                "\nContenuto " + contenuto + " " + noteContenuto +

                "\nGradevolezza " + gradevolezza + " " + noteGradevolezza +
                "\nOriginalità " + originalita + " " + noteOriginalita +
                "\nEdizione " + edizione + " " + noteEdizione +
                "\nFinale " + finale + " " + noteFinale;
    }
}