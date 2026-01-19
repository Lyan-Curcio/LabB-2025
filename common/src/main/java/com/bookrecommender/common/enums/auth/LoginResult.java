package com.bookrecommender.common.enums.auth;

/**
 * Enumerazione che definisce i possibili esiti di un tentativo di login da parte di un utente.
 * <p>
 * Ogni costante è associata a un messaggio descrittivo in lingua italiana,
 * utile per essere mostrato direttamente nell'interfaccia utente (GUI) del client.
 * </p>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public enum LoginResult {

    /** Indica che le credenziali sono valide e l'accesso è stato autorizzato. */
    OK("Accesso effettuato con successo"),

    /** Indica che l'utente esiste, ma la password fornita non corrisponde. */
    INCORRECT_PASSWORD("La password non è corretta"),

    /** Indica che non è stato trovato alcun utente registrato con l'ID fornito. */
    USER_ID_NOT_FOUND("Non esiste un utente con l'user id inserito"),

    /** Indica un errore di sistema imprevisto (es. problemi di connessione al database). */
    UNEXPECTED_ERROR("Errore non previsto!");

    /**
     * Messaggio descrittivo associato al codice di errore o di successo.
     */
    private final String msg;

    /**
     * Costruttore privato dell'enumerazione.
     *
     * @param msg il messaggio descrittivo da associare alla costante
     */
    LoginResult(String msg) {
        this.msg = msg;
    }

    /**
     * Restituisce il messaggio descrittivo associato all'esito del login.
     *
     * @return una stringa contenente il messaggio da mostrare all'utente
     */
    public String getMessage() {
        return msg;
    }
}