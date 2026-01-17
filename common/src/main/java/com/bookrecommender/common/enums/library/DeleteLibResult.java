package com.bookrecommender.common.enums.library;

public enum DeleteLibResult {
    OK("Libreria eliminata"),
    LIBRARY_NOT_FOUND("La libreria che stai provando ad eliminare non esiste"),
    UNEXPECTED_ERROR("Errore non previsto!");

    private final String msg;

    DeleteLibResult(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}
