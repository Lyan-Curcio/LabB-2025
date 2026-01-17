package com.bookrecommender.common.enums.suggestion;

public enum AddSuggestionResult {
    OK("Consiglio aggiunto"),
    MAIN_BOOK_NOT_IN_LIBRARY("Il libro a cui si sta aggiungendo un consigliato non è in nessuna delle tue librerie"),
    SUGGESTED_BOOK_NOT_IN_LIBRARY("Il libro consigliato non è in nessuna delle tue librerie"),
    ALREADY_SUGGESTED("Questo libro è già stato consigliato"),
    UNEXPECTED_ERROR("Errore non previsto!");

    private final String msg;

    AddSuggestionResult(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}
