package com.bookrecommender.common.enums.library;

public enum AddBookToLibResult {
    OK("Libro aggiunto alla libreria"),
    LIBRARY_NOT_FOUND("La libreria specificata non esiste"),
    BOOK_ALREADY_IN_LIBRARY("Il libro è già presente nella libreria"),
    UNEXPECTED_ERROR("Errore non previsto!");

    private final String msg;

    AddBookToLibResult(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}
