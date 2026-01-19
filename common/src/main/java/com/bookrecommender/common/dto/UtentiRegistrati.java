package com.bookrecommender.common.dto;

import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe DTO (Data Transfer Object) che rappresenta l'entità di un Utente Registrato.
 * <p>
 * Questa classe è immutabile e contiene le informazioni anagrafiche dell'utente.
 * Implementa <code>Serializable</code> per essere trasferita tra client e server durante le fasi di login e registrazione.
 * Non contiene la password, che viene gestita separatamente per motivi di sicurezza.
 * </p>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public class UtentiRegistrati implements Serializable {

    /** Versione della classe per la serializzazione. */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Identificativo univoco (username) scelto dall'utente. */
    public final String userId;

    /** Nome di battesimo dell'utente. */
    public final String nome;

    /** Cognome dell'utente. */
    public final String cognome;

    /** Codice Fiscale dell'utente. */
    public final String codiceFiscale;

    /** Indirizzo email dell'utente. */
    public final String email;

    /**
     * Costruisce un nuovo oggetto <code>UtentiRegistrati</code> con i dati anagrafici specificati.
     * <p>
     * Questo costruttore viene usato tipicamente durante la fase di registrazione lato client,
     * prima dell'invio dei dati al server.
     * </p>
     *
     * @param userId        l'username o ID univoco
     * @param nome          il nome proprio
     * @param cognome       il cognome
     * @param codiceFiscale il codice fiscale
     * @param email         l'indirizzo di posta elettronica
     */
    public UtentiRegistrati(String userId, String nome, String cognome, String codiceFiscale, String email) {
        this.userId = userId;
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.email = email;
    }

    /**
     * Costruisce un oggetto <code>UtentiRegistrati</code> estraendo i dati da un <code>ResultSet</code> SQL.
     * <p>
     * Questo costruttore mappa le colonne del database sui campi dell'oggetto.
     * In caso di <code>SQLException</code> (es. colonna mancante), viene generato un oggetto
     * con campi vuoti e l'errore viene stampato sullo standard error.
     * </p>
     *
     * @param rs il <code>ResultSet</code> posizionato sulla riga dell'utente da leggere
     */
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

    /**
     * Restituisce una rappresentazione testuale completa dell'utente per scopi di debug.
     * <p>
     * Include username, nome completo, codice fiscale ed email formattati su più righe.
     * </p>
     *
     * @return una stringa multilinea con tutti i dettagli dell'utente
     */
    public String toStringDebug() {
        return userId + ": " + nome + " " + cognome +
                "\n\t" + codiceFiscale +
                "\n\t" + email;
    }

    /**
     * Restituisce una stringa sintetica per la visualizzazione all'utente.
     * <p>
     * Formato: "Nome Cognome (CodiceFiscale)"
     * </p>
     *
     * @return una stringa formattata con le informazioni principali
     */
    public String toStringInfo() {
        return nome + " " + cognome + " (" + codiceFiscale + ")";
    }
}