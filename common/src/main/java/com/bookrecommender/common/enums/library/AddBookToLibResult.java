package com.bookrecommender.common.enums.library;

/**
 * Enumerazione che definisce i possibili esiti dell'operazione di aggiunta di un libro a una libreria personale.
 * <p>
 * Questa enum viene utilizzata per gestire i casi in cui l'inserimento non va a buon fine,
 * ad esempio se la libreria non esiste o se il libro è già presente al suo interno.
 * </p>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public enum AddBookToLibResult {

    /** Il libro è stato aggiunto correttamente alla libreria specificata. */
    OK("Libro aggiunto alla libreria"),

    /** La libreria indicata (tramite ID) non è stata trovata nel database. */
    LIBRARY_NOT_FOUND("La libreria specificata non esiste"),

    /** Il libro selezionato è già presente all'interno della libreria di destinazione (evita duplicati). */
    BOOK_ALREADY_IN_LIBRARY("Il libro è già presente nella libreria"),

    /** Si è verificato un errore imprevisto (es. eccezione SQL o problema di connessione). */
    UNEXPECTED_ERROR("Errore non previsto!");

    /**
     * Messaggio descrittivo associato all'esito dell'operazione.
     */
    private final String msg;

    /**
     * Costruttore privato dell'enumerazione.
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