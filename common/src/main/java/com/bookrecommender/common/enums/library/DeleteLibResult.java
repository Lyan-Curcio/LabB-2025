package com.bookrecommender.common.enums.library;

/**
 * Enum che definisce i possibili esiti dell'operazione di eliminazione di una libreria personale.
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public enum DeleteLibResult {

    /** La libreria è stata eliminata correttamente. */
    OK("Libreria eliminata"),

    /**
     * L'operazione è fallita perché la libreria specificata non è associata all'utente o non esiste.
     */
    LIBRARY_NOT_FOUND("La libreria che stai provando ad eliminare non esiste"),

    /** Si è verificato un errore imprevisto. */
    UNEXPECTED_ERROR("Errore non previsto!");

    /**
     * Messaggio descrittivo associato all'esito.
     */
    private final String msg;

    /**
     * Costruttore privato dell'enum.
     *
     * @param msg il messaggio descrittivo da associare alla costante
     */
    DeleteLibResult(String msg) {
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