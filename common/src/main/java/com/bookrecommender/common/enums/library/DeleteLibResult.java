package com.bookrecommender.common.enums.library;

/**
 * Enumerazione che definisce i possibili esiti dell'operazione di eliminazione di una libreria personale.
 * <p>
 * Viene utilizzata per confermare la rimozione di una libreria o segnalare errori,
 * come il tentativo di eliminare una risorsa non esistente.
 * </p>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public enum DeleteLibResult {

    /** La libreria è stata eliminata correttamente dal database. */
    OK("Libreria eliminata"),

    /**
     * L'operazione è fallita perché la libreria specificata non esiste.
     * Potrebbe essere stata già eliminata o l'ID fornito non è valido.
     */
    LIBRARY_NOT_FOUND("La libreria che stai provando ad eliminare non esiste"),

    /** Si è verificato un errore imprevisto (es. eccezione SQL) durante l'eliminazione. */
    UNEXPECTED_ERROR("Errore non previsto!");

    /**
     * Messaggio descrittivo associato all'esito.
     */
    private final String msg;

    /**
     * Costruttore privato dell'enumerazione.
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