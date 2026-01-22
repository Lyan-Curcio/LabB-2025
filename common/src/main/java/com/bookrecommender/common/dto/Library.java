package com.bookrecommender.common.dto;

import java.io.Serial;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Library implements Serializable {

    /** Versione della classe per la serializzazione. */
    @Serial
    private static final long serialVersionUID = 1L;

    public final int id;
    public final String name;
    public final String userId;

    public Library(String name, String userId) {
        id = -1;
        this.name = name;
        this.userId = userId;
    }

    public Library(ResultSet rs) {
        int _id;
        String _name;
        String _userId;

        try {
            _id = rs.getInt("id");
            _name = rs.getString("nome");
            _userId = rs.getString("userid");
        }
        catch (SQLException e) {
            System.err.println("Impossibile costruire una 'Library' con il 'ResultSet': " + rs);
            _id = 0;
            _name = "";
            _userId = "";
        }

        this.id = _id;
        this.name = _name;
        this.userId = _userId;
    }

    public String toStringDebug() {
        return id + ": " + name + " di " + userId;
    }
}
