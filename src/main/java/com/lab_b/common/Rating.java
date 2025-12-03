package main.java.com.lab_b.common;

import java.io.Serializable;

public class Rating implements Serializable {
    private static final long serialVersionUID = 1L;

    private int libroId;
    private String userId;

    // Punteggi 1-5
    private int stile;
    private int contenuto;
    private int gradevolezza;
    private int originalita;
    private int edizione;
    
    // Note testuali (max 256 char)
    private String noteStile;
    private String noteContenuto;
    private String noteGradevolezza;
    private String noteOriginalita;
    private String noteEdizione;

    // Metodo di utilità per il calcolo della media [cite: 35]
    public int getVotoFinale() {
        double media = (stile + contenuto + gradevolezza + originalita + edizione) / 5.0;
        return (int) Math.round(media);
    }
    
    // Costruttori, Getters e Setters
}