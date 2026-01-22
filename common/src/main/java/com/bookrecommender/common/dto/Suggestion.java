package com.bookrecommender.common.dto;

import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Suggestion implements Serializable {
    /** Versione della classe per la serializzazione. */
    @Serial
    private static final long serialVersionUID = 1L;

    public final int id;
    public final String userId;
    public final int mainBookId;
    public final int suggestedBookId;

    public Suggestion(String userId, int mainBookId, int suggestedBookId) {
        id = -1;
        this.userId = userId;
        this.mainBookId = mainBookId;
        this.suggestedBookId = suggestedBookId;
    }

    public Suggestion(ResultSet rs) {
        int _id;
        String _userId;
        int _mainBookId;
        int _suggestedBookId;

        try {
            _id = rs.getInt("id");
            _userId = rs.getString("userid");
            _mainBookId = rs.getInt("libro_sorgente_id");
            _suggestedBookId = rs.getInt("libro_consigliato_id");
        }
        catch (SQLException e) {
            System.err.println("Impossibile costruire una 'Suggestion' con il 'ResultSet': " + rs);
            _id = -1;
            _userId = "";
            _mainBookId = 0;
            _suggestedBookId = 0;
        }

        this.id = _id;
        this.userId = _userId;
        this.mainBookId = _mainBookId;
        this.suggestedBookId = _suggestedBookId;
    }
}
