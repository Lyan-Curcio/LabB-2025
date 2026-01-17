package com.bookrecommender.common.enums.rating;

public enum CreateRatingResult {
    OK("Valutazione creata"),
    BOOK_NOT_IN_LIBRARY("Questo libro non è in nessuna delle tue librerie"),
    ALREADY_RATED("Questo libro ha già una valutazione"),
    UNEXPECTED_ERROR("Errore non previsto!");

    private final String msg;

    CreateRatingResult(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}
