package com.bookrecommender.common.extended_dto;

import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AverageRatings implements Serializable {

    /** Versione della classe per la serializzazione. */
    @Serial
    private static final long serialVersionUID = 1L;

    public final int ratingsCount;

    public final float stile;
    public final float contenuto;
    public final float gradevolezza;
    public final float originalita;
    public final float edizione;
    public final float finale;

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
            System.err.println("Impossibile costruire un 'AverageRatings' con il 'ResultSet': " + rs);
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