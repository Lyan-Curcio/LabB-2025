package com.lab_b.common;

import java.io.Serializable;
import java.util.List;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String titolo;
    private List<String> autori; // O una stringa unica csv, a seconda del parsing
    private int annoPubblicazione;
    private String editore;
    private String categoria;

    // Costruttori, Getters e Setters
    public Book(int id, String titolo, List<String> autori, int anno) {
        this.id = id;
        this.titolo = titolo;
        this.autori = autori;
        this.annoPubblicazione = anno;
    }
    
    // toString per visualizzazione in liste
    @Override
    public String toString() {
        return titolo + " (" + annoPubblicazione + ")";
    }
}