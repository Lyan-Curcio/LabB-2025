package com.bookrecommender.common.enums.auth;

/**
 * Enum che definisce i possibili esiti di una richiesta di registrazione di un nuovo utente.
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public enum RegisterResult {

    /** Indica che la registrazione è stata completata con successo e il nuovo utente è stato creato. */
    OK("Registrazione effettuata con successo"),

    /** Indica un errore dovuto al fatto che l'User ID scelto è già in uso da un altro utente. */
    DUPLICATE_USERID("L'user id scelto non è disponibile"),

    /** Indica un errore dovuto al fatto che il Codice Fiscale inserito è già presente nel sistema. */
    DUPLICATE_CF("Esiste già un altro utente con lo stesso codice fiscale"),

    /** Indica un errore dovuto al fatto che l'indirizzo email inserito è già associato a un altro account. */
    DUPLICATE_EMAIL("Esiste già un utente con la stessa email"),

    /** Si è verificato un errore imprevisto. */
    UNEXPECTED_ERROR("Errore non previsto!");

    /**
     * Messaggio descrittivo associato all'esito della registrazione.
     */
    private final String msg;

    /**
     * Costruttore privato dell'enum.
     *
     * @param msg il messaggio descrittivo da associare alla costante
     */
    RegisterResult(String msg) {
        this.msg = msg;
    }

    /**
     * Restituisce il messaggio descrittivo associato all'esito.
     *
     * @return una stringa contenente il messaggio da mostrare all'utente
     */
    public String getMessage() {
        return msg;
    }
}