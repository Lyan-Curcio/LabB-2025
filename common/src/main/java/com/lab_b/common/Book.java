package com.lab_b.common;

import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Book implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public final int id;
    public final String titolo;
    public final String[] autori;
    public final int annoPubblicazione;
    public final String editore;
    public final String[] categorie;

    // Costruttori, Getters e Setters
    public Book(int id, String titolo, String[] autori, int anno, String editore, String[] categorie) {
        this.id = id;
        this.titolo = titolo;
        this.autori = autori;
        this.annoPubblicazione = anno;
        this.editore = editore;
        this.categorie = categorie;
    }

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

    public String toStringDebug() {
        return id + ": " + titolo +
            "\n\t" + String.join(", ", autori) +
            "\n\t" + annoPubblicazione +
            "\n\t" + editore +
            "\n\t" + String.join(", ", categorie);
    }

    public String toStringInfo() {
        return titolo + " - Da: " + String.join(", ", autori) + " - (" + annoPubblicazione + ")";
    }


    private String[] autoriParser(String autoriStr) {
        if (autoriStr == null || autoriStr.isEmpty()) {
            return new String[0];
        }

        return autoriStr.split(",");
    }

    private String[] categorieParser(String categorieStr) {
        if (categorieStr == null || categorieStr.isEmpty()) {
            return new String[0];
        }

        return categorieStr.split(",");
    }
}