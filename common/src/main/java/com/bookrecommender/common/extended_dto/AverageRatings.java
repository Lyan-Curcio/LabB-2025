package com.bookrecommender.common.extended_dto;

import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe che rappresenta le valutazioni medie di un libro.
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class AverageRatings implements Serializable {

    /**
     * Versione della classe per la serializzazione.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Numero totale di valutazioni.
     */
    public final int ratingsCount;

    /**
     * Valutazione media dello stile.
     */
    public final float stile;

    /**
     * Valutazione media del contenuto.
     */
    public final float contenuto;

    /**
     * Valutazione media della gradevolezza.
     */
    public final float gradevolezza;

    /**
     * Valutazione media dell'originalità.
     */
    public final float originalita;

    /**
     * Valutazione media dell'edizione.
     */
    public final float edizione;

    /**
     * Valutazione media finale.
     */
    public final float finale;

    /**
     * Costruisce un {@code AverageRatings} a partire da un {@link ResultSet}.
     *
     * @param rs {@link ResultSet} contenente i valori medi delle valutazioni
     */
    public AverageRatings(ResultSet rs) {
        int _ratingsCount;
        float _stile;
        float _contenuto;
        float _gradevolezza;
        float _originalita;
        float _edizione;
        float _finale;

        try {
            _ratingsCount = rs.getInt("count");
            _stile = rs.getFloat("stile");
            _contenuto = rs.getFloat("contenuto");
            _gradevolezza = rs.getFloat("gradevolezza");
            _originalita = rs.getFloat("originalita");
            _edizione = rs.getFloat("edizione");
            _finale = rs.getFloat("finale");
        }
        catch (SQLException e) {
            System.err.println(
                "Impossibile costruire un 'AverageRatings' con il 'ResultSet': " + rs
            );
            _ratingsCount = 0;
            _stile = 0;
            _contenuto = 0;
            _gradevolezza = 0;
            _originalita = 0;
            _edizione = 0;
            _finale = 0;
        }

        this.ratingsCount = _ratingsCount;
        this.stile = _stile;
        this.contenuto = _contenuto;
        this.gradevolezza = _gradevolezza;
        this.originalita = _originalita;
        this.edizione = _edizione;
        this.finale = _finale;
    }
}
