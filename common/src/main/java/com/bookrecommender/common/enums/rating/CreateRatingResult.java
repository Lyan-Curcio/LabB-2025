package com.bookrecommender.common.enums.rating;

/**
 * Enum che definisce i possibili esiti del tentativo di inserire una nuova valutazione per un libro.
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public enum CreateRatingResult {

    /** La valutazione è stata creata con successo. */
    OK("Valutazione creata"),

    /**
     * L'operazione è fallita perché il libro indicato non è presente in nessuna delle librerie dell'utente.
     * Un utente può valutare solo i libri che ha aggiunto ad almeno a una delle proprie librerie.
     */
    BOOK_NOT_IN_LIBRARY("Questo libro non è in nessuna delle tue librerie"),

    /**
     * L'operazione è fallita perché esiste già una valutazione per questo libro da parte dell'utente.
     */
    ALREADY_RATED("Questo libro ha già una valutazione"),

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
    CreateRatingResult(String msg) {
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