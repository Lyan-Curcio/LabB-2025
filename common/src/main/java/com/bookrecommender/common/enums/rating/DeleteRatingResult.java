package com.bookrecommender.common.enums.rating;

public enum DeleteRatingResult {
    OK("Valutazione eliminata"),
    NOT_RATED("Questo libro non è stato ancora valutato"),
    UNEXPECTED_ERROR("Errore non previsto!");

    private final String msg;

    DeleteRatingResult(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}
