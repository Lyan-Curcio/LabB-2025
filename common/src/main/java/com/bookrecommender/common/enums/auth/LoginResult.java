package com.bookrecommender.common.enums.auth;

/**
 * Enum che definisce i possibili esiti di un tentativo di login da parte di un utente.
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

    /** Si è verificato un errore imprevisto. */
    UNEXPECTED_ERROR("Errore non previsto!");

    /**
     * Messaggio descrittivo associato al codice di errore o di successo.
     */
    private final String msg;

    /**
     * Costruttore privato dell'enum.
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