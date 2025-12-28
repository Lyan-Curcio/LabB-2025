package com.lab_b.common;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String nome;
    private String cognome;
    private String codiceFiscale;
    private String email;
    // La password non dovrebbe viaggiare in chiaro nell'oggetto User completo, 
    // ma gestita separatamente durante il login.

    // Costruttori, Getters e Setters
}