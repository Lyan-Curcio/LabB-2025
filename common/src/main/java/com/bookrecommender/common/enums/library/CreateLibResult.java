package com.bookrecommender.common.enums.library;

/**
 * Enumerazione che definisce i possibili esiti della creazione di una nuova libreria personale.
 * <p>
 * Viene utilizzata come valore di ritorno dal metodo <code>creaLibreria</code> dell'interfaccia autenticata.
 * Permette di distinguere il caso di successo dai casi di errore, come il tentativo di usare un nome già esistente.
 * </p>
 *
 * @author Lorenzo Monachino 757393 VA
 * @author Lyan Curcio 757579 VA
 * @author Sergio Saldarriaga 757394 VA
 * @author Nash Guizzardi 756941 VA
 */
public enum CreateLibResult {

    /** La nuova libreria è stata creata e salvata correttamente nel database. */
    OK("Libreria creata"),

    /**
     * Impossibile creare la libreria perché l'utente possiede già una libreria con lo stesso nome.
     * I nomi delle librerie devono essere univoci per ogni utente.
     */
    DUPLICATE_NAME("Esiste già una libreria con questo nome"),

    /** Si è verificato un errore imprevisto (es. eccezione SQL o errore di sistema). */
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
    CreateLibResult(String msg) {
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