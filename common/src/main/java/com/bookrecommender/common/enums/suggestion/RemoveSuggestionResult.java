package com.bookrecommender.common.enums.suggestion;

public enum RemoveSuggestionResult {
    OK("Consiglio eliminato"),
    NOT_SUGGESTED("Questo libro non è stato consigliato"),
    UNEXPECTED_ERROR("Errore non previsto!");

    private final String msg;

    RemoveSuggestionResult(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}
