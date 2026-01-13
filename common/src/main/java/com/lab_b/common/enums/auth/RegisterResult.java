package com.lab_b.common.enums.auth;

public enum RegisterResult {
    OK("Registrazione effettuata con successo"),
    DUPLICATE_USERID("L'user id scelto non è disponibile"),
    DUPLICATE_CF("Esiste già un altro utente con lo stesso codice fiscale"),
    DUPLICATE_EMAIL("Esiste già un utente con la stessa email"),
    UNEXPECTED_ERROR("Errore non previsto!");

    private final String msg;

    RegisterResult(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}
