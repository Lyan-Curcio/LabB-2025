package com.bookrecommender.common.enums.rating;

/**
 * Enum che definisce i possibili esiti del tentativo di rimuovere una valutazione.
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public enum DeleteRatingResult {

    /** La valutazione è stata eliminata correttamente. */
    OK("Valutazione eliminata"),

    /** L'operazione è fallita perché non esiste alcuna valutazione associata all'utente per questo libro. */
    NOT_RATED("Questo libro non è stato ancora valutato"),

    /** Si è verificato un errore imprevisto. */
    UNEXPECTED_ERROR("Errore non previsto!");

    /** Messaggio descrittivo associato all'esito. */
    private final String msg;

    /**
     * Costruttore privato dell'enum.
     *
     * @param msg il messaggio descrittivo da associare alla costante
     */
    DeleteRatingResult(String msg) {
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