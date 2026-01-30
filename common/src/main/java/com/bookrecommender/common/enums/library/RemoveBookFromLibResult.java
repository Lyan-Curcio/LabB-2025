package com.bookrecommender.common.enums.library;

/**
 * Enum che definisce i possibili esiti dell'operazione di rimozione di un libro da una libreria personale.
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public enum RemoveBookFromLibResult {

    /** Il libro è stato rimosso correttamente dalla libreria specificata. */
    OK("Libro rimosso dalla libreria"),

    /** L'operazione è fallita perché la libreria specificata (tramite ID) non è associata all'utente o non esiste. */
    LIBRARY_NOT_FOUND("La libreria specificata non esiste"),

    /**
     * L'operazione è fallita perché il libro indicato non è presente nella libreria.
     */
    BOOK_NOT_IN_LIBRARY("Il libro non è presente nella libreria"),

    /**
     * L'operazione è fallita perché il libro indicato è solo in una libreria ed esiste uan recensione o dei consigliati collegati a esso.
     */
    BOOK_IS_SUGGESTED_OR_RATED("Il libro è solo in questa libreria e c'è una recensione o dei consigliati che dipendono da esso"),

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
    RemoveBookFromLibResult(String msg) {
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