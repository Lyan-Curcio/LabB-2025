package com.bookrecommender.common.enums.suggestion;

/**
 * Enumerazione che definisce i possibili esiti dell'operazione di rimozione di un suggerimento.
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public enum RemoveSuggestionResult {

    /** Il suggerimento è stato rimosso correttamente dal sistema. */
    OK("Consiglio eliminato"),

    /** Non esiste alcun suggerimento registrato tra i due libri specificati. */
    NOT_SUGGESTED("Questo libro non è stato consigliato"),

    /** Si è verificato un errore imprevisto. */
    UNEXPECTED_ERROR("Errore non previsto!");

    /** Messaggio descrittivo associato all'esito. */
    private final String msg;

    /**
     * Costruttore privato dell'enumerazione.
     *
     * @param msg il messaggio descrittivo da associare alla costante
     */
    RemoveSuggestionResult(String msg) {
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