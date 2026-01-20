package com.bookrecommender.common.enums.library;

/**
 * Enum che definisce i possibili esiti dell'operazione di aggiunta di un libro a una libreria personale.
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public enum AddBookToLibResult {

    /** Il libro è stato aggiunto correttamente alla libreria specificata. */
    OK("Libro aggiunto alla libreria"),

    /** La libreria indicata (tramite ID) non è associata all'utente indicato o non esiste nel database. */
    LIBRARY_NOT_FOUND("La libreria specificata non esiste"),

    /** Il libro selezionato è già presente all'interno della libreria di destinazione (evita duplicati). */
    BOOK_ALREADY_IN_LIBRARY("Il libro è già presente nella libreria"),

    /** Si è verificato un errore imprevisto. */
    UNEXPECTED_ERROR("Errore non previsto!");

    /**
     * Messaggio descrittivo associato all'esito dell'operazione.
     */
    private final String msg;

    /**
     * Costruttore privato dell'enum.
     *
     * @param msg il messaggio descrittivo da associare alla costante
     */
    AddBookToLibResult(String msg) {
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