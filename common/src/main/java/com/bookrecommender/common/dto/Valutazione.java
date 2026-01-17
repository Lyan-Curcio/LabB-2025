package com.bookrecommender.common.dto;

import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Valutazione implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public final int id;
    public final int libroId;
    public final String userId;

    // Punteggi 1-5
    public final int stile;
    public final int contenuto;
    public final int gradevolezza;
    public final int originalita;
    public final int edizione;
    public final int finale; // (calcolato con la media)
    
    // Note testuali (max 256 char)
    public final String noteStile;
    public final String noteContenuto;
    public final String noteGradevolezza;
    public final String noteOriginalita;
    public final String noteEdizione;
    public final String noteFinale;

    // Costruttori, Getters e Setters
    public Valutazione(
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

    public Valutazione(ResultSet rs) {
        int _id, _libroId;
        String _userId;
        int _stile, _contenuto, _gradevolezza, _originalita, _edizione, _finale;
        String _noteStile, _noteContenuto, _noteGradevolezza, _noteOriginalita, _noteEdizione, _noteFinale;

        try {
            _id = rs.getInt("id");
            _libroId = rs.getInt("libro_id");
            _userId = rs.getString("user_id");

            _stile = rs.getInt("stile");
            _contenuto = rs.getInt("contenuto");
            _gradevolezza = rs.getInt("gradevolezza");
            _originalita = rs.getInt("originalita");
            _edizione = rs.getInt("edizione");
            _finale = Math.round((_stile + _contenuto + _gradevolezza + _originalita + _edizione) / 5.0f);

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

    public String toStringDebug() {
        return id + ": Da '" + userId + "' per '" + libroId + "'" +
            "\n\tStile " + stile + " " + noteStile +
            "\n\tContenuto " + contenuto + " " + noteContenuto +
            "\n\tGradevolezza " + gradevolezza + " " + noteGradevolezza +
            "\n\tOriginalità " + originalita + " " + noteOriginalita +
            "\n\tEdizione " + edizione + " " + noteEdizione +
            "\n\tFinale " + finale + " " + noteFinale;
    }

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