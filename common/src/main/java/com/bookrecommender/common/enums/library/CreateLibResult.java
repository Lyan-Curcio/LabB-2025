package com.bookrecommender.common.enums.library;

public enum CreateLibResult {
    OK("Libreria creata"),
    DUPLICATE_NAME("Esiste già una libreria con questo nome"),
    UNEXPECTED_ERROR("Errore non previsto!");

    private final String msg;

    CreateLibResult(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}
