package com.lab_b.common.dto;

import java.io.Serializable;

public class ValutazioniLibri implements Serializable {
    private static final long serialVersionUID = 1L;

    private int libroId;
    private String userId;

    // Punteggi 1-5
    private int stile;
    private int contenuto;
    private int gradevolezza;
    private int originalita;
    private int edizione;
    private int finale;
    
    // Note testuali (max 256 char)
    private String noteStile;
    private String noteContenuto;
    private String noteGradevolezza;
    private String noteOriginalita;
    private String noteEdizione;
    private String noteFinale;

    // Costruttori, Getters e Setters
}