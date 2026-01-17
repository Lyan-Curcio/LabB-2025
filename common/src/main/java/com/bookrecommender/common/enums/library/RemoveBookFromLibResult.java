package com.bookrecommender.common.enums.library;

public enum RemoveBookFromLibResult {
    OK("Libro aggiunto alla libreria"),
    LIBRARY_NOT_FOUND("La libreria specificata non esiste"),
    BOOK_NOT_IN_LIBRARY("Il libro non è presente nella libreria"),
    UNEXPECTED_ERROR("Errore non previsto!");

    private final String msg;

    RemoveBookFromLibResult(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}
