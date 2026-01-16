package com.bookrecommender.common.enums.auth;

public enum LoginResult {
    OK("Accesso effettuato con successo"),
    INCORRECT_PASSWORD("La password non è corretta"),
    USER_ID_NOT_FOUND("Non esiste un utente con l'user id inserito"),
    UNEXPECTED_ERROR("Errore non previsto!");

    private final String msg;

    LoginResult(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}
