package com.bookrecommender.common.enums.suggestion;

/**
 * Enum che definisce i possibili esiti dell'operazione di aggiunta di un suggerimento.
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public enum AddSuggestionResult {

    /** Il suggerimento è stato registrato con successo. */
    OK("Consiglio aggiunto"),

    /** Il libro "sorgente" non è presente in nessuna delle librerie dell'utente.
     * Un utente può aggiungere consigliati solo ai libri che ha aggiunto ad almeno a una delle proprie librerie.
     */
    MAIN_BOOK_NOT_IN_LIBRARY("Il libro a cui si sta aggiungendo un consigliato non è in nessuna delle tue librerie"),

    /** Il libro che si intende consigliare non è presente in nessuna delle librerie dell'utente.
     * Un utente può consigliare solo libri che ha aggiunto ad almeno a una delle proprie librerie.
     */
    SUGGESTED_BOOK_NOT_IN_LIBRARY("Il libro consigliato non è in nessuna delle tue librerie"),

    /** Esiste già un suggerimento identico inserito dall'utente. */
    ALREADY_SUGGESTED("Questo libro è già stato consigliato"),

    /** Esistono già 3 suggerimenti per il libro sorgente */
    TOO_MANY_SUGGESTIONS("Questo libro ha già 3 consigliati"),

    /** Si è verificato un errore imprevisto. */
    UNEXPECTED_ERROR("Errore non previsto!");

    /** Messaggio descrittivo associato all'esito. */
    private final String msg;

    /**
     * Costruttore privato dell'enum.
     *
     * @param msg il messaggio descrittivo da associare alla costante
     */
    AddSuggestionResult(String msg) {
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