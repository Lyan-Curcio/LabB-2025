package com.bookrecommender.common.dto;

import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtentiRegistrati implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public final String userId;
    public final String nome;
    public final String cognome;
    public final String codiceFiscale;
    public final String email;
    // La password non dovrebbe viaggiare in chiaro nell'oggetto User completo, 
    // ma gestita separatamente durante il login.

    // Costruttori, Getters e Setters
    public UtentiRegistrati(String userId, String nome, String cognome, String codiceFiscale, String email)
    {
        this.userId = userId;
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.email = email;
    }

    public UtentiRegistrati(ResultSet rs) {
        String _userId;
        String _nome;
        String _cognome;
        String _codiceFiscale;
        String _email;

        try {
            _userId = rs.getString("userid");
            _nome = rs.getString("nome");
            _cognome = rs.getString("cognome");
            _codiceFiscale = rs.getString("codice_fiscale");
            _email = rs.getString("email");
        }
        catch (SQLException e) {
            System.err.println("Impossibile costruire un 'User' con il 'ResultSet': " + rs);
            _userId = "";
            _nome = "";
            _cognome = "";
            _codiceFiscale = "";
            _email = "";
        }

        this.userId = _userId;
        this.nome = _nome;
        this.cognome = _cognome;
        this.codiceFiscale = _codiceFiscale;
        this.email = _email;
    }

    public String toStringDebug() {
        return userId + ": " + nome + " " + cognome +
            "\n\t" + codiceFiscale +
            "\n\t" + email;
    }

    public String toStringInfo() {
        return nome + " " + cognome + " (" + codiceFiscale + ")";
    }
}